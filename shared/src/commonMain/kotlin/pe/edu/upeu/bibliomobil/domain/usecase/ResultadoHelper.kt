package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

/**
 * Función helper de conveniencia para envolver la ejecución de bloques de lógica de negocio
 * en un tipo estándar [Result], capturando excepciones controladas y relanzando la cancelación.
 */
inline fun <T> resultadoDe(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
