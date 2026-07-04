# 📱 Tarea 2: Sockets TCP Concurrente - La ITV del Infierno 🚗🔥💀
**BK Programación** | Proyecto desarrollado por: **Irene Condado Alcantarilla**

## 📋 Descripción del Proyecto
Este proyecto implementa una aplicación distribuida **Cliente-Servidor utilizando Sockets TCP multihilo en Java**. La aplicación simula el caótico y exigente funcionamiento de una estación de Inspección Técnica de Vehículos (ITV) conocida como *"La ITV del Infierno"*. 

El sistema destaca por dos mecánicas principales:
1. **Exigencia Extrema:** Los vehículos deben aprobar el 100% de las fases para obtener la pegatina.
2. **El "Factor Cuñao":** Los inspectores (hilos del servidor) detestan las frases pretenciosas o de 'cuñao'. Si el cliente (hilo cliente) responde de forma inadecuada, el inspector se enfadará y reducirá permanentemente las probabilidades del vehículo de superar el resto de pruebas.

---

## 🏗️ Arquitectura del Sistema

El desarrollo está compuesto por tres pilares fundamentales que interactúan mediante la red:

1. **Estación ITV (Servidor):** Administra un pool limitado de **4 líneas de inspección**. Controla el acceso de los vehículos de manera concurrente; si todas las líneas están ocupadas, los sockets clientes quedan en una cola de espera TCP hasta que se libere un box.
2. **Inspectores ITV (Hilos Servidor):** Cada vez que un coche entra a una línea, un hilo servidor dedicado toma el control, le asigna un "walkie-talkie" virtual (flujo de entrada/salida de datos) y lo guía a través de la inspección.
3. **Coches (Hilos Cliente / Simulador):** Representan a los conductores que interactúan en tiempo real enviando respuestas aleatorias al servidor tras recibir las órdenes de las pruebas.

---

## 🔄 Flujo de la Simulación (Paso a Paso)

    - El cliente se conecta al servidor.
    - Si hay alguna línea libre, puede pasar; si todas están ocupadas, el cliente espera hasta que una línea se libere.
    - Cuando el coche entra, el inspector le envía el mensaje: “Buenas tardes, le dejo aquí el walkie‑talkie para darle órdenes”
    - El cliente no contestará a esta primera frase.
    - El inspector realiza, una a una, las siguientes pruebas de la ITV:
        * Luces
        * Frenos
        * Emisiones
        * Dirección
        * Suspensión
        * El inspector tardará entre 1 y 5 segundos en realizar cada prueba.
    - Para cada prueba el inspector le dirá por el walkie, "Realice la prueba: Luces/Frenos/...." a lo que el cliente, siempre le responderá.
    - Cada prueba tiene 60% de probabilidad de salir bien. Sin embargo, si el cliente responde con una frase incluida en la lista de frases de 'cuñao', el inspector se enfadará y reducirá la probabilidad global del coche en un 10%. Esa probabilidad reducida permanece para el resto de pruebas.
    - Cuando el inspector termine de realizar todas las pruebas, le mandará el mensaje de “Le retiro el walkie, esperé en la puerta. Gracias.” al cliente. El cliente no responderá, esperará nervioso al mensaje final. 
    - El inspector, dependiendo de si ha superado o no la prueba, le dirá el mensaje final "Tome su pegatina" o "Debe volver de nuevo".

* Las peleas que puedan ocurrir entre el inspector y el cliente después de notificarle que debe volver de nuevo, quedan fuera del alcance de esta tarea.
---
*Desarrollado como parte del módulo de Programación de Servicios y Procesos (PSP).*
