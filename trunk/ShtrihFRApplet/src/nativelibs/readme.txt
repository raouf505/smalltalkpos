Let $ROOT$ means directory, where your main jnlp file (one you using in <APPLET> tag) are situated

1) unpack nativeLibsForRXTXComm.jnlp to the $ROOT$ directory
2) unpack librxtxSerial-i386.dll.jar and librxtxSerial-i386.so.jar to $ROOT$/lib/
3) add line <extension name="nativeLibsForRXTXComm" href="nativeLibsForRXTXComm.jnlp"/> to your main jnlp file into <resources> section

Executing this instructions:
a) should guarantee this applet will work on computers with no rxtx .dll's or .so's installed
b) it will force java web start to load those libraries in "correct way", so it will continue working after reloading page (normally it fails trying to load already loaded library again)