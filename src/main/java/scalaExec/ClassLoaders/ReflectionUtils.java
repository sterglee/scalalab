
package scalaExec.ClassLoaders;
import java.util.*;
import java.lang.reflect.*;

public class ReflectionUtils {

   /**
      Prints all constructors of a class
      @param cl a class
    */
   public static String getConstructors(Class cl)
   {  
       String clName = cl.getName();
       Class  superClass = cl.getSuperclass();
       String superClassName="";
       if (superClass != null) superClassName = superClass.getName();
       StringBuilder constr=new StringBuilder("\n\nConstructors for class: "+clName+"  inherits  "+superClassName+" \n");
       Constructor[] constructors = cl.getDeclaredConstructors();

      for (Constructor c : constructors)
      {           
         String name = c.getName();
         constr.append("     " + Modifier.toString(c.getModifiers()));
         constr.append(" " + name + "(");

         // print parameter types
         Class[] paramTypes = c.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {  
            if (j > 0) constr.append(", ");
            constr.append(paramTypes[j].getName());
         }
         constr.append("); \n");
      }
      return constr.toString();
   }

   /**
      Prints all methods of a class
      @param cl a class
    */
   public static String getMethods(Class cl)
   {  

       String clName = cl.getName();
       StringBuilder methodsBuild = new StringBuilder("\n\nMethods for class: "+clName+"\n");
       
       Method[] methods = cl.getDeclaredMethods();

      for (Method m : methods)
      {  
         Class retType = m.getReturnType();
         String name = m.getName();

         // print modifiers, return type and method name
         methodsBuild.append("     " + Modifier.toString(m.getModifiers()));
         methodsBuild.append(" " + retType.getName() + " " + name + "(");

         // print parameter types
         Class[] paramTypes = m.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {  
            if (j > 0) methodsBuild.append(", ");
            methodsBuild.append(paramTypes[j].getName());
         }
         methodsBuild.append(");\n");
      }
      return  methodsBuild.toString();
   }

   /**
      Prints all fields of a class
      @param cl a class
    */
   public static String getFields(Class cl)
   {  
      StringBuilder fieldsBuild = new StringBuilder("\n\nFields of class: "+cl.getName()+"\n");
      Field[] fields = cl.getDeclaredFields();

      for (Field f : fields)
      {  
         Class type = f.getType();
         String name = f.getName();
         fieldsBuild.append("     " + Modifier.toString(f.getModifiers()));
         fieldsBuild.append("  " + type.getName() + " " + name + ";\n");
      }
      return fieldsBuild.toString();
   }
   
}


