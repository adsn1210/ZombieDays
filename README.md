Hola,

Este proyecto cuenta actualmente con un único personaje jugable, aunque según los requisitos debería incluir dos jugadores. Esta decisión fue tomada para priorizar la calidad técnica y la estabilidad del juego, centrándome en implementar correctamente las mecánicas principales antes de escalar la complejidad.

El aspecto más destacable del juego es que cada partida es diferente. El laberinto no está predefinido, sino que se genera dinámicamente en cada ejecución mediante un algoritmo DFS (Depth-First Search), lo que garantiza que nunca habrá dos laberintos iguales.

Además, el sistema de movimiento incluye modificadores de velocidad, tanto por teclado como por colisiones con objetos. El jugador puede correr manteniendo pulsada la tecla SHIFT o moverse más despacio con CTRL, lo que permite un mayor control y añade profundidad al gameplay.

El objetivo del proyecto ha sido aplicar de forma correcta conceptos clave como gestión de estados del juego, colisiones, temporización, generación procedural y arquitectura MVC, priorizando un código claro, modular y ampliable.