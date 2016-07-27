#JNIDemo
	
**这是一个基于Android studio 的JNIdemo，仅供学习**

##新建工程
	
*	新建项目工程JNIDemo，把工程结构调成 Project，如下图所示：
		
	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/project.png);

##配置环境

1.	**找到gradle.properties,在下面加上一行<font color=red>android.useDeprecatedNdk=true</font>，解决ndk提示版本低不能自动编译jni。不管高版本低版本都加上，都是不会错的。如下图所示**
	
	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/properties.png)
		

2.	**找到app目录下的build.gradle，在defaultConfig里面加上，这里的作用相当与makefile**
		
		ndk {
			//在JNI打log 必须加上log,否则会报错log函数未定义,
            ldLibs "log"
            //指定生成模块名字,也就是最终的动态库名aplex,相应库文件名libaplex.so moduleName "aplex"
            moduleName "aplex"
            // 指定生成哪些处理器架构的动态库文件，如果要运行在x86架构处理器一定需要指定 abiFilters "armeabi" , "x86"
            abiFilters "armeabi", "x86", "armeabi-v7a"
        }
	如下图所示
	
	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/buildgradle.png)

##编译工程

1.	**新建一个类JniNative，并在类中写两个native方法。**

		public class JniNative {
		    static {
		        System.loadLibrary("aplex");
		    }
		    public static native void sayHelloToC();
		    public static native String HelloFromC();
		}
	
	>**这里要注意：System.loadLibrary("aplex")会去data\data\packagname\lib下面去找libaplex.so库**

2.	**把.java文件编译成.class文件**
	
	>**选择整个项目，找到导航栏，做如下操作：Build->Make Modlues app。** 

3.	**把.class文件编译成.h文件**
	*	打开命令行插件Terminal
	*	进入app/build/inermediates/classes/debug目录下
	*	使用命令javah 包名+类名 把.class文件编译成.h文件
	*	这个.h文件会生成在\app\build\intermediates\classes\debug目录下，我的.h文件叫com_example_aplex_jnidemo_JniNative.h
	
	如图所示：
		
	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/cmd.png)

4.	**整理编写jni**
	
	* 在app\src\main 下面新建一个名叫jni的目录
	* 把刚刚生成的.h文件拷到jni目录中
	* jni目录下创建一个.c文件 
	* 把com_example_aplex_jnidemo_JniNative.h内容拷到.c文件中，并且实现.h文件中的函数定义
	

			/* DO NOT EDIT THIS FILE - it is machine generated */
			#include <jni.h>
			#include <stdio.h>
			#include <stdlib.h>
			/* Header for class com_example_aplex_jnidemo_JniNative */
			
			#ifndef _Included_com_example_aplex_jnidemo_JniNative
			#define _Included_com_example_aplex_jnidemo_JniNative
			
			#include "android/log.h"
			static const char *TAG="JNIDEMO";
			#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
			
			#ifdef __cplusplus
			extern "C" {
			#endif
			/*
			 * Class:     com_example_aplex_jnidemo_JniNative
			 * Method:    sayHelloToC
			 * Signature: ()V
			 */
			JNIEXPORT void JNICALL Java_com_example_aplex_jnidemo_JniNative_sayHelloToC
			        (JNIEnv * env, jclass clazz,jstring hellostring){
			        char* pStr = NULL;
			
			        jclass     jstrObj   = (*env)->FindClass(env, "java/lang/String");
			        jstring    encode    = (*env)->NewStringUTF(env, "utf-8");
			        jmethodID  methodId  = (*env)->GetMethodID(env, jstrObj, "getBytes", "(Ljava/lang/String;)[B");
			        jbyteArray byteArray = (jbyteArray)(*env)->CallObjectMethod(env, hellostring, methodId, encode);
			        jsize      strLen    = (*env)->GetArrayLength(env, byteArray);
			        jbyte      *jBuf     = (*env)->GetByteArrayElements(env, byteArray, JNI_FALSE);
			        if (jBuf > 0)
			        {
			                pStr = (char*)malloc(strLen + 1);
			
			                if (!pStr)
			                {
			                        return;
			                }
			                memcpy(pStr, jBuf, strLen);
			               
			                pStr[strLen] = 0;
			        }
			 	LOGE("%s",pStr);
			        (*env)->ReleaseByteArrayElements(env, byteArray, jBuf, 0);
			
			}
			
			/*
			 * Class:     com_example_aplex_jnidemo_JniNative
			 * Method:    HelloFromC
			 * Signature: ()Ljava/lang/String;
			 */
			JNIEXPORT jstring JNICALL Java_com_example_aplex_jnidemo_JniNative_HelloFromC
			        (JNIEnv * env, jclass clazz){
			        char * hello = "hello";
			        return  (*env)->NewStringUTF(env,hello);
			}
			
			#ifdef __cplusplus
			}
			#endif
			#endif

5.	**在MainActivity中调用JNI**

		public void sayHelloToc(View view){
        	JniNative.sayHelloToC("Hello C");
	    }
	    public void HelloFromC(View view){
	        Toast.makeText(MainActivity.this, JniNative.HelloFromC(), Toast.LENGTH_SHORT).show();
	    }

6.	**效果,如图所示:** 

	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/hellofromc.png)
		
	![](https://github.com/chengcnaplex/JNIDemo/blob/master/img/sayhellotoc.png)
