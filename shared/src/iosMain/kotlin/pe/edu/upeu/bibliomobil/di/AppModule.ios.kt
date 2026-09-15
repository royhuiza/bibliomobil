package pe.edu.upeu.bibliomobil.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    // Definiciones específicas para la plataforma iOS de ser requeridas en el futuro
}

/**
 * Punto de entrada para inicializar Koin de forma nativa desde la aplicación iOS (Swift).
 */
fun initKoinIos() = initKoin {}
