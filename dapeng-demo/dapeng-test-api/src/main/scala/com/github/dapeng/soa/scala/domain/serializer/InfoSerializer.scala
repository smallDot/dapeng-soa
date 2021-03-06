 package com.github.dapeng.soa.scala.domain.serializer;

        import com.github.dapeng.soa.scala.domain.serializer._;
        import com.github.dapeng.core._
        import com.github.dapeng.org.apache.thrift._
        import com.github.dapeng.org.apache.thrift.protocol._

        /**
        * Autogenerated by Dapeng-Code-Generator (1.2.2)
        *
        * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
        *  @generated
        **/

        class InfoSerializer extends BeanSerializer[com.github.dapeng.soa.scala.domain.Info]{
          
      @throws[TException]
      override def read(iprot: TProtocol): com.github.dapeng.soa.scala.domain.Info = {

        var schemeField: com.github.dapeng.org.apache.thrift.protocol.TField = null
        iprot.readStructBegin()

      var name: String = null
        var code: String = null
        

      while (schemeField == null || schemeField.`type` != com.github.dapeng.org.apache.thrift.protocol.TType.STOP) {

        schemeField = iprot.readFieldBegin

        schemeField.id match {
          
              case 1 =>
                  schemeField.`type` match {
                    case com.github.dapeng.org.apache.thrift.protocol.TType.STRING => name = iprot.readString
                    case _ => com.github.dapeng.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 2 =>
                  schemeField.`type` match {
                    case com.github.dapeng.org.apache.thrift.protocol.TType.STRING => code = iprot.readString
                    case _ => com.github.dapeng.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
          case _ => com.github.dapeng.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
        }
      }

      iprot.readFieldEnd
      iprot.readStructEnd

      val bean = com.github.dapeng.soa.scala.domain.Info(name = name,code = code)
      validate(bean)

      bean
      }
    
      @throws[TException]
      override def write(bean: com.github.dapeng.soa.scala.domain.Info, oprot: TProtocol): Unit = {

      validate(bean)
      oprot.writeStructBegin(new com.github.dapeng.org.apache.thrift.protocol.TStruct("Info"))

      
            {
            val elem0 = bean.name 
            oprot.writeFieldBegin(new com.github.dapeng.org.apache.thrift.protocol.TField("name", com.github.dapeng.org.apache.thrift.protocol.TType.STRING, 1.asInstanceOf[Short]))
            oprot.writeString(elem0)
            oprot.writeFieldEnd
            
            }
            {
            val elem1 = bean.code 
            oprot.writeFieldBegin(new com.github.dapeng.org.apache.thrift.protocol.TField("code", com.github.dapeng.org.apache.thrift.protocol.TType.STRING, 2.asInstanceOf[Short]))
            oprot.writeString(elem1)
            oprot.writeFieldEnd
            
            }
      oprot.writeFieldStop
      oprot.writeStructEnd
    }
    
      @throws[TException]
      override def validate(bean: com.github.dapeng.soa.scala.domain.Info): Unit = {
      
              if(bean.name == null)
              throw new SoaException(SoaCode.NotNull, "name字段不允许为空")
            
              if(bean.code == null)
              throw new SoaException(SoaCode.NotNull, "code字段不允许为空")
            
    }
    

          @throws[TException]
          override def toString(bean: com.github.dapeng.soa.scala.domain.Info): String = if (bean == null) "null" else bean.toString

        }
        
      