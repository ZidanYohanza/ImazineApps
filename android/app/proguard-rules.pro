# Get rid of package names, makes file smaller
-repackageclasses

# --> Imazine
-keep class com.himatifunpad.imazine.core.domain.model.** { *; }
-keep class com.himatifunpad.imazine.core.data.remote.json.** { *; }
-keep class com.himatifunpad.imazine.core.data.ParcelizedPost

# --> ViewBinding
# ViewBindingDelegate uses Reflection.
-keepclassmembers class ** implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);
}

# --> Retrofit/Okhttp
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepattributes *Annotation*, Signature, Exceptions