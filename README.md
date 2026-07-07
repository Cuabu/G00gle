# 📱 Android WebApp Simple (WebView)

Este repositorio contiene la estructura básica para crear una aplicación de Android que funcione exclusivamente para abrir una página web HTTPS. 

Para que la aplicación funcione correctamente, normalmente solo es necesario modificar **3 archivos** principales en tu proyecto de Android Studio:

1. **`AndroidManifest.xml`** → Para agregar el permiso de conexión a Internet.
2. **`activity_main.xml`** → Para definir el diseño visual y colocar el componente WebView.
3. **`MainActivity.kt`** → Para configurar el comportamiento del WebView y cargar la URL deseada.

A continuación, se detalla el paso a paso de cada modificación.

---

## 1️⃣ Permiso de Internet (`AndroidManifest.xml`)

Para que la aplicación pueda cargar una página web externa, necesita tener acceso a la red. Abre tu archivo `AndroidManifest.xml` y agrega la siguiente línea de permisos. Debe ir dentro de la etiqueta `<manifest>` pero **antes** de la etiqueta `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
