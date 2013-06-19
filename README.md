DedicaMNS
=========

Aplicación android para la imputación de horas en Media Net Software, a traves de la web de dedicaciones corporativa: http://dedicaciones.medianet.es

Ya está subida a google play! https://play.google.com/store/apps/details?id=com.edwise.dedicamns

El user y password se guardan en las sharedpreferences de la aplicación, si dejamos marcado el check de "recordarme". No se hace nada más con el pass (revisa el código si eres muy paranoico, ahi lo tienes ;) )

La aplicación está en versión final.

Se agradecen cualquier tipo de sugerencias o aviso de bugs.

Consideraciones importantes a tener en cuenta:
  - El listado de proyectos y subproyectos se carga la primera vez que te logas en la app, y se guarda en una base de datos. El listado puede "actualizarse" con la opción "Recarga de proyectos".
  - Github ya no permite sección de donwloads, así que ya no puedo subir ahí el apk. Instalatela por google play. También puedes bajarte todo el código, y crearte tu el apk en un entorno con el sdk.
 
Futuras ampliaciones / evolutivos:    
  - Nueva opción de imputación batch, más personalizable: entre dos fechas dadas, y con horas personalizables.
  - Opción de menu contextual en el listado mensual, para poder copiar o borrar días.
  - Perfiles en la imputación batch.
  
Ampliaciones / evolutivos ya realizados:
  - Elegir mes y año en listado mensual, para poder consultar otros meses.
  - Permitir más de un proyecto por día en el listado mensual y ventana de detalle.
  - Permitir introducir la hora en más formatos (ahora solo permite "HH:MI").
  - Adaptar toda la app para "landscape" (actualmente hay pantallas que no salen bien en apaisado)
  - Mostrar informe mensual de horas.
  - Opción de compartir el informe mensual.
  - Optimización de carga de proyectos, guardandolos en una bd en el primer login. 
  
  

