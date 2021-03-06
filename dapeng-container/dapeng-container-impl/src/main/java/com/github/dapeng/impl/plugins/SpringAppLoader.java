package com.github.dapeng.impl.plugins;

import com.github.dapeng.api.Container;
import com.github.dapeng.api.ContainerFactory;
import com.github.dapeng.api.Plugin;
import com.github.dapeng.core.*;
import com.github.dapeng.core.definition.SoaServiceDefinition;
import com.github.dapeng.impl.container.DapengApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpringAppLoader implements Plugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAppLoader.class);
    private final Container container;
    private final List<ClassLoader> appClassLoaders;
    private List<Object> springCtxs = new ArrayList<>();

    public SpringAppLoader(Container container, List<ClassLoader> appClassLoaders) {
        this.container = container;
        this.appClassLoaders = appClassLoaders;
    }

    @Override
    public void start() {
        LOGGER.warn("Plugin::SpringAppLoader start.");
        String configPath = "META-INF/spring/services.xml";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (ClassLoader appClassLoader : appClassLoaders) {
            try {

                // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new Object[]{xmlPaths.toArray(new String[0])});
                // context.start();
                Class<?> appCtxClass = appClassLoader.loadClass("org.springframework.context.support.ClassPathXmlApplicationContext");
                Class<?>[] parameterTypes = new Class[]{String[].class};
                Constructor<?> constructor = appCtxClass.getConstructor(parameterTypes);

                Thread.currentThread().setContextClassLoader(appClassLoader);
                Object springCtx = getSpringContext(configPath, appClassLoader, constructor);

                springCtxs.add(springCtx);

                Method method = appCtxClass.getMethod("getBeansOfType", Class.class);

                Map<String, SoaServiceDefinition<?>> processorMap = (Map<String, SoaServiceDefinition<?>>)
                        method.invoke(springCtx, appClassLoader.loadClass(SoaServiceDefinition.class.getName()));
                //TODO: 需要构造Application对象
                Map<String, ServiceInfo> appInfos = toServiceInfos(processorMap);
                Application application = new DapengApplication(appInfos.values().stream().collect(Collectors.toList()),
                        appClassLoader);

                Map<ProcessorKey, SoaServiceDefinition<?>> serviceDefinitionMap = toSoaServiceDefinitionMap(appInfos, processorMap);
                container.registerAppProcessors(serviceDefinitionMap);

                // IApplication app = new ...
                if (!application.getServiceInfos().isEmpty()) {
                    container.registerApplication(application);
                    container.registerAppMap(toApplicationMap(serviceDefinitionMap, application));
                }

                LOGGER.info(" ------------ SpringClassLoader: " + ContainerFactory.getContainer().getApplications());

                //Start spring context
                LOGGER.info(" start to boot app");
                Method startMethod = appCtxClass.getMethod("start");
                startMethod.invoke(springCtx);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                Thread.currentThread().setContextClassLoader(classLoader);
            }
        }
    }

    private Map<ProcessorKey, Application> toApplicationMap(Map<ProcessorKey,
            SoaServiceDefinition<?>> serviceDefinitionMap, Application application) {

        Map<ProcessorKey, Application> map = serviceDefinitionMap.keySet().stream().
                collect(Collectors.toMap(Function.identity(), processorKey -> application));
        return map;
    }

    @Override
    public void stop() {
        LOGGER.warn("Plugin:SpringAppLoader stop.");
        LOGGER.warn("Gracefully shutdown not implemented yet");
        // TODO stop or close??
//        springCtxs.forEach(springCtx -> {
//            LOGGER.info(" stop unload app");
//            try {
//                Method stopMethod = springCtx.getClass().getMethod("stop");
//                stopMethod.invoke(springCtx);
//            } catch (NoSuchMethodException e) {
//                LOGGER.error(e.getMessage(), e);
//            } catch (IllegalAccessException e) {
//                LOGGER.error(e.getMessage(), e);
//            } catch (InvocationTargetException e) {
//                LOGGER.error(e.getMessage(), e);
//            }
//        });
    }

    private Map<String, ServiceInfo> toServiceInfos(Map<String, SoaServiceDefinition<?>> processorMap)
            throws Exception {

        Map<String, ServiceInfo> serviceInfoMap = new HashMap<>(processorMap.size());
        for (Map.Entry<String, SoaServiceDefinition<?>> processorEntry : processorMap.entrySet()) {
            String processorKey = processorEntry.getKey();
            SoaServiceDefinition<?> processor = processorEntry.getValue();

            long count = new ArrayList<>(Arrays.asList(processor.iface.getClass().getInterfaces()))
                    .stream()
                    .filter(m -> "org.springframework.aop.framework.Advised".equals(m.getName()))
                    .count();

            Class<?> ifaceClass = (Class) (count > 0 ?
                    processor.iface.getClass().getMethod("getTargetClass").invoke(processor.iface) :
                    processor.iface.getClass());

            Service service = processor.ifaceClass.getAnnotation(Service.class);
            assert (service != null); // TODO

            ServiceInfo serviceInfo = new ServiceInfo(service.name(), service.version(),
                    "service", ifaceClass);
            serviceInfoMap.put(processorKey, serviceInfo);
        }

        return serviceInfoMap;
    }


    private Map<ProcessorKey, SoaServiceDefinition<?>> toSoaServiceDefinitionMap(
            Map<String, ServiceInfo> serviceInfoMap,
            Map<String, SoaServiceDefinition<?>> processorMap) {

        Map<ProcessorKey, SoaServiceDefinition<?>> serviceDefinitions = serviceInfoMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new ProcessorKey(entry.getValue().serviceName, entry.getValue().version),
                        entry -> processorMap.get(entry.getKey())));
        return serviceDefinitions;
    }


    private Object getSpringContext(String configPath, ClassLoader appClassLoader, Constructor<?> constructor) throws Exception {
        List<String> xmlPaths = new ArrayList<>();

        Enumeration<URL> resources = appClassLoader.getResources(configPath);

        while (resources.hasMoreElements()) {
            URL nextElement = resources.nextElement();
            // not load dapeng-soa-transaction-impl
            if (!nextElement.getFile().matches(".*dapeng-transaction-impl.*")) {
                xmlPaths.add(nextElement.toString());
            }
        }
        Object context = constructor.newInstance(new Object[]{xmlPaths.toArray(new String[0])});
        return context;
    }

}
