# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/viewlift/Library/AppCMSAndroidUIRest/sdk/tools/proguard/proguard-appCMSAndroidUI.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.appCMSAndroidUI.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep class com.viewlift.AppCMSApplication { *;}
-keep class com.urbanairship.Autopilot { *;}

-keep public interface com.viewlift.models.network.rest.** { *;}

# rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

## Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

## Required even for GCM only apps
-dontwarn com.amazon.device.messaging.**

## Autopilot
-keep public class * extends com.urbanairship.Autopilot

## Actions
-keep public class * extends com.urbanairship.actions.Action
-keep public class * implements com.urbanairship.actions.ActionRegistry$Predicate

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

#kiswe class
-keep class com.kiswe.kmsdkcorekit { *;}
-keep class veg.mediaplayer.sdk.** { *;}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keep class com.google.** { *;}
-keep interface com.google.** { *;}
-keep class android.support.** { *; }
-keep class com.viewlift.casting.** { *; }

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment thbuiis to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn org.apache.tools.ant.**
-dontwarn org.jaxen.**
-dontwarn sun.misc.**
-dontwarn com.google.android.gms.location.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn java.nio.file.**
-dontwarn android.app.**
-dontwarn android.content.pm.PackageManager.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-dontskipnonpubliclibraryclasses
-optimizationpasses 3

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
  public static *** d(...);
  public static *** w(...);
  public static *** v(...);
  public static *** i(...);
}
