
      package com.github.dapeng.soa.service;

      import com.github.dapeng.core.Processor;
      import com.github.dapeng.core.Service;
      import com.github.dapeng.core.SoaGlobalTransactional;

      /**
       * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

      * 
      **/
      @Service(name="com.github.dapeng.soa.service.CalculateService",version = "1.0.0")
      @Processor(className = "com.github.dapeng.soa.CalculateServiceCodec$Processor")
      public interface CalculateService {
      
          /**
          * 
          **/
          
             
             Integer calcualteWordCount( String filename, String word) throws com.github.dapeng.core.SoaException;
            
        
          /**
          * 
          **/
          
             
             java.util.Map<String, Integer> calcualteWordsCount( String fileName) throws com.github.dapeng.core.SoaException;
            
        
      }
      