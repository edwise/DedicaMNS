DedicaMNS
=========

Aplicación android para la imputación de horas en Media Net Software, a traves de la web de dedicaciones corporativa: http://dedicaciones.medianet.es

Ya está subida a google play! https://play.google.com/store/apps/details?id=com.edwise.dedicamns

El user y password se guardan en las sharedpreferences de la aplicación, si dejamos marcado el check de "recordarme". No se hace nada más con el pass (revisa el código si eres muy paranoico, ahi lo tienes ;) )

La aplicación esta en ya en versión final (RC por ahora :P ).

Se agradecen cualquier tipo de sugerencias o aviso de bugs.

Consideraciones importantes a tener en cuenta:
  - Se carga el listado de proyectos y subproyectos una vez en cada "sesión". Esto se hace la primera vez que accedemos a cualquiera de las 2 opciones. Luego ya se queda cacheado. Por eso el primer acceso puede ser algo lento...
  - Github ya no permite sección de donwloads, así que ya no puedo subir ahí el apk. Instalatela por google play. También puedes bajarte todo el código, y crearte tu el apk en un entorno con el sdk. (esta última opción es recomendable para los paranoicos que crean que en el apk el código es distinto y en él, me guardo su password de alguna manera rara xD )
 
Futuras ampliaciones / evolutivos:
  - Elegir mes y año en listado mensual, para poder consultar otros meses. (HECHO!)
  - Permitir más de un proyecto por día en el listado mensual y ventana de detalle. (HECHO!)
  - Permitir introducir la hora en más formatos (ahora solo permite "HH:MI"). (HECHO!)
  - Adaptar toda la app para "landscape" (actualmente hay pantallas que no salen bien en apaisado) (HECHO!)
  - Optimización de carga de proyectos ¿? guardandolos en una bd, quizá?  
  - Perfiles en la imputación batch
  - Mostrar informe mensual de horas
  
  

