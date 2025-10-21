Emir Taboada
Issue 1.1: Una data class es la mejor opción porque el objetivo de esta clase no es tener lógica compleja, sino representar y manejar datos de manera concisa, segura y eficiente.
Issue 1.3: PuntoDeReciclaje y TipoMaterial tienen una relación de composición.
Un PuntoDeReciclaje contiene una lista de materiales (TipoMaterial) que acepta, pero no es un TipoMaterial.
Esto permite que cada punto de reciclaje sea flexible: algunos aceptan solo plástico y papel, otros aceptan todos, etc.S