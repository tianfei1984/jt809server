 package com.ltmonitor.jt809.server;
 
 import java.nio.charset.Charset;
 import org.apache.mina.core.session.IoSession;
 import org.apache.mina.filter.codec.ProtocolCodecFactory;
 import org.apache.mina.filter.codec.ProtocolDecoder;
 import org.apache.mina.filter.codec.ProtocolEncoder;
 
 public class TiamaesMessageCodecFactory
   implements ProtocolCodecFactory
 {
   private final TiamaesMessageDecoder decoder;
   private final TiamaesMessageEncoder encoder;
 
   public TiamaesMessageCodecFactory()
   {
     this.decoder = new TiamaesMessageDecoder(Charset.forName("utf-8"));
     this.encoder = new TiamaesMessageEncoder(Charset.forName("utf-8"));
   }
 
   public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
     return this.decoder;
   }
 
   public ProtocolEncoder getEncoder(IoSession arg0) throws Exception
   {
     return this.encoder;
   }
 }

