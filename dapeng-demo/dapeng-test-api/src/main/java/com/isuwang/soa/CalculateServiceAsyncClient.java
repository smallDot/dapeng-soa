package com.github.dapeng.soa;

      import com.github.dapeng.core.*;
      import com.github.dapeng.org.apache.thrift.*;
      import java.util.concurrent.CompletableFuture;
      import java.util.concurrent.Future;
      import java.util.ServiceLoader;
      import com.github.dapeng.soa.CalculateServiceCodec.*;
      import com.github.dapeng.soa.service.CalculateServiceAsync;

      /**
       * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

      **/
      public class CalculateServiceAsyncClient implements CalculateServiceAsync{
      private final String serviceName;
      private final String version;

      private SoaConnectionPool pool;

      public CalculateServiceAsyncClient() {
        this.serviceName = "com.github.dapeng.soa.service.CalculateService";
        this.version = "1.0.0";

        ServiceLoader<SoaConnectionPoolFactory> factories = ServiceLoader.load(SoaConnectionPoolFactory.class);
        for (SoaConnectionPoolFactory factory: factories) {
          this.pool = factory.getPool();
          break;
        }
        this.pool.registerClientInfo(serviceName,version);
      }

      
          
            /**
            * 
            **/
            
              public CompletableFuture<Integer> calcualteWordCount(String filename,String word, long timeout) throws SoaException{

              String methodName = "calcualteWordCount";
              calcualteWordCount_args calcualteWordCount_args = new calcualteWordCount_args();
              calcualteWordCount_args.setFilename(filename);
                calcualteWordCount_args.setWord(word);
                

              CompletableFuture<calcualteWordCount_result> response = (CompletableFuture<calcualteWordCount_result>) pool.sendAsync(serviceName,version,"calcualteWordCount",calcualteWordCount_args, new CalcualteWordCount_argsSerializer(), new CalcualteWordCount_resultSerializer(),timeout);

              
                  
                      return response.thenApply((calcualteWordCount_result result )->  result.getSuccess());
                    
                
            }
            
          

        
          
            /**
            * 
            **/
            
              public CompletableFuture<java.util.Map<String, Integer>> calcualteWordsCount(String fileName, long timeout) throws SoaException{

              String methodName = "calcualteWordsCount";
              calcualteWordsCount_args calcualteWordsCount_args = new calcualteWordsCount_args();
              calcualteWordsCount_args.setFileName(fileName);
                

              CompletableFuture<calcualteWordsCount_result> response = (CompletableFuture<calcualteWordsCount_result>) pool.sendAsync(serviceName,version,"calcualteWordsCount",calcualteWordsCount_args, new CalcualteWordsCount_argsSerializer(), new CalcualteWordsCount_resultSerializer(),timeout);

              
                  
                      return response.thenApply((calcualteWordsCount_result result )->  result.getSuccess());
                    
                
            }
            
          

        

      /**
      * getServiceMetadata
      **/
      public String getServiceMetadata() throws SoaException {
        String methodName = "getServiceMetadata";
        getServiceMetadata_args getServiceMetadata_args = new getServiceMetadata_args();
        getServiceMetadata_result response = pool.send(serviceName,version,methodName,getServiceMetadata_args, new GetServiceMetadata_argsSerializer(), new GetServiceMetadata_resultSerializer());
        return response.getSuccess();
      }

    }
    