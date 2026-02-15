package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;

public class CustomPipelineUtil {
   private static Method DECODE_METHOD;
   private static Method ENCODE_METHOD;
   private static Method MTM_DECODE;
   private static Method MTM_ENCODE;

   public static void init() {
      Class channelHandlerContextClass = SpigotReflectionUtil.getNettyClass("channel.ChannelHandlerContext");

      try {
         DECODE_METHOD = SpigotReflectionUtil.BYTE_TO_MESSAGE_DECODER.getDeclaredMethod("decode", channelHandlerContextClass, SpigotReflectionUtil.BYTE_BUF_CLASS, List.class);
         DECODE_METHOD.setAccessible(true);
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      }

      try {
         ENCODE_METHOD = SpigotReflectionUtil.MESSAGE_TO_BYTE_ENCODER.getDeclaredMethod("encode", channelHandlerContextClass, Object.class, SpigotReflectionUtil.BYTE_BUF_CLASS);
         ENCODE_METHOD.setAccessible(true);
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      }

      Class messageToMessageEncoderClass;
      try {
         messageToMessageEncoderClass = SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageDecoder");
         MTM_DECODE = messageToMessageEncoderClass.getDeclaredMethod("decode", channelHandlerContextClass, Object.class, List.class);
         MTM_DECODE.setAccessible(true);
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      }

      try {
         messageToMessageEncoderClass = SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageEncoder");
         MTM_ENCODE = messageToMessageEncoderClass.getDeclaredMethod("encode", channelHandlerContextClass, Object.class, List.class);
         MTM_ENCODE.setAccessible(true);
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      }

   }

   public static List<Object> callDecode(Object decoder, Object ctx, Object input) throws InvocationTargetException {
      ArrayList output = new ArrayList();

      try {
         DECODE_METHOD.invoke(decoder, ctx, input, output);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

      return output;
   }

   public static void callEncode(Object encoder, Object ctx, Object msg, Object output) throws InvocationTargetException {
      try {
         ENCODE_METHOD.invoke(encoder, ctx, msg, output);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

   }

   public static List<Object> callMTMEncode(Object encoder, Object ctx, Object msg) {
      ArrayList output = new ArrayList();

      try {
         MTM_ENCODE.invoke(encoder, ctx, msg, output);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
      }

      return output;
   }

   public static List<Object> callMTMDecode(Object decoder, Object ctx, Object msg) throws InvocationTargetException {
      ArrayList output = new ArrayList();

      try {
         MTM_DECODE.invoke(decoder, ctx, msg, output);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

      return output;
   }
}
