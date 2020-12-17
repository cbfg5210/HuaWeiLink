# 不应该被混淆的代码:
# AndroidManifest.xml引用的类(四大组件等)。
# JNI调用的Java方法(当 JNI 调用的 Java 方法被混淆后，方法名会变成无意义的名称，这就与 C++ 中原本的 Java 方法名不匹配，因而会无法找到所调用的方法)。
# Java 的 native 方法
# 被反射的元素(被反射使用的类、变量、方法、包名等)。
# WebView中JavaScript使用的类。
# Layout文件引用的自定义View。
# 枚举
# 实体类(即常说的"数据类"，经常伴随着序列化与反序列化操作)
# 项目中引用的第三方库(这点要按第三方库的混淆说明配置)

#指定代码优化级别，值在0-7之间，默认为5
-optimizationpasses 5

# google推荐算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

#记录生成的日志数据,gradle build时在本项目根目录输出
#apk 包内所有 class 的内部结构
-dump output/class_files.txt
#未混淆的类和成员
-printseeds output/seeds.txt
#列出从 apk 中删除的代码
-printusage output/unused.txt
#混淆前后的映射
-printmapping output/mapping.txt

# 不忽略库中的非public的类成员
-dontskipnonpubliclibraryclassmembers

# 避免混淆Annotation、内部类、泛型、匿名类
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 保持四大组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保持support下的所有类及其内部类
-keep class androidx.appcompat.** {*;}

# 保留继承的
-keep public class * extends androidx.annotation.**

# 保持自定义控件
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保留序列化的类
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# native 方法不混淆
-keep class * {
    native <methods>;
}

#移除log代码
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}