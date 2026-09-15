package pe.edu.upeu.bibliomobil.domain.usecase

/**
 * Función helper de conveniencia para envolver la ejecución de bloques de lógica de negocio
 * en un tipo estándar [Result], capturando cualquier excepción controlada.
 */
inline fun <T> resultadoDe(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
