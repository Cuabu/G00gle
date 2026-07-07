package com.example.g00gle

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    // URLs constantes
    companion object {
        private const val PAGINA_WEB = "https://present-divine-cub.ngrok-free.app"
        private const val ALQUILER = "http://192.168.110.5/alquiler%20vehicular/index.php"
        private const val BASE_DATOS = "http://192.168.110.5/phpmyadmin/"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarWebView()
        configurarMenuLateral()
        configurarNavegacionHaciaAtras()

        // Carga inicial solo si no venimos de un cambio de orientación
        if (savedInstanceState == null) {
            webView.loadUrl(PAGINA_WEB)
        }
    }

    private fun inicializarVistas() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        webView = findViewById(R.id.webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView() {
        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                builtInZoomControls = false
                displayZoomControls = false
            }

            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    // Solo mostrar mensaje de error en el marco principal
                    if (request?.isForMainFrame == true) {
                        view?.loadData(
                            """
                            <html>
                            <body style="font-family:Arial;text-align:center;padding:40px;color:#333;">
                                <h3>No se pudo cargar la página</h3>
                                <p>Verifica tu conexión a la red local o que el servidor esté activo.</p>
                            </body>
                            </html>
                            """.trimIndent(),
                            "text/html",
                            "UTF-8"
                        )
                    }
                }
            }
        }
    }

    private fun configurarMenuLateral() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open, // Recomendable crear estos strings en res/values/strings.xml
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio, R.id.nav_web -> webView.loadUrl(PAGINA_WEB)
                R.id.nav_alquiler -> webView.loadUrl(ALQUILER)
                R.id.nav_bd -> webView.loadUrl(BASE_DATOS)
                R.id.nav_acerca -> mostrarAcercaDe()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun mostrarAcercaDe() {
        val htmlContent = """
            <html>
            <body style="font-family:Arial;text-align:center;padding:30px;color:#222;">
                <h2>EMULADOR DE APPS</h2>
                <p>Aplicación desarrollada para fines académicos.</p>
            </body>
            </html>
        """.trimIndent()
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun configurarNavegacionHaciaAtras() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    // 1. Si el menú lateral está abierto, ciérralo
                    drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    // 2. Si el WebView puede retroceder, regresa al historial web
                    webView.canGoBack() -> {
                        webView.goBack()
                    }
                    // 3. Comportamiento normal (salir de la actividad)
                    else -> {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}