# Stacking Items 🏆

## Introducción
**Stacking Items** es un simulador visual y un solucionador algorítmico inspirado en el famoso problema "Stacking Cups" (Problema J) de la Maratón Internacional de Programación ICPC 2025. El proyecto permite a los usuarios interactuar con torres formadas por diferentes tipos de tazas y tapas, además de calcular cómo apilarlas para alcanzar una altura objetivo exacta.

Este proyecto fue desarrollado por **Juan Diego Gaitán** y **Oscar Lasso**, estudiantes de quinto semestre de la **Escuela Colombiana de Ingeniería Julio Garavito**, como parte de la asignatura de Desarrollo Orientado por Objetos.

---

## Metodología
El proyecto se construyó de manera progresiva, dividiendo el trabajo en **ciclos de desarrollo** (mini-ciclos). Se adoptaron enfoques de la **Programación Ágil y Extreme Programming (XP)**, priorizando siempre que el código fuera fácil de leer, de probar y, sobre todo, fácil de extender en el futuro.

El progreso se dividió de la siguiente manera:
1. **Ciclo 1 - La Base:** Se construyó el simulador básico que permitía crear una torre, apilar tazas y tapas regulares, y consultar la altura total.
2. **Ciclo 2 - Interacción:** Se agregaron funcionalidades para intercambiar objetos de lugar, tapar tazas automáticamente y buscar movimientos para reducir la altura de la torre.
3. **Ciclo 3 - El Solucionador (Maratón):** Se implementó el algoritmo capaz de resolver el problema original de la maratón, calculando el orden exacto de las tazas para lograr una altura específica e integrándolo visualmente al simulador.
4. **Ciclo 4 - Extensión Compleja:** Se introdujeron nuevos tipos de tazas (Normales, Abridoras, Jerárquicas) y tapas (Normales, Miedosas, Locas). Esto puso a prueba la capacidad del código para aceptar nuevas reglas sin dañar lo que ya funcionaba.
5. **Cierre y Calidad:** Aunque inicialmente se proponía Eclipse, el equipo optó por utilizar **IntelliJ IDEA** como entorno de desarrollo. Se aseguró la calidad del proyecto integrando **JaCoCo** para garantizar un cubrimiento de pruebas unitarias superior al 75%, y **PMD** para aplicar reglas estrictas de código limpio. Todas las pruebas se diseñaron bajo el patrón **AAA (Arrange, Act, Assert)** para asegurar que cada parte del código hiciera exactamente lo que debía hacer.

---

## Diseño
El diseño arquitectónico del proyecto se pensó para separar la "apariencia" (lo visual) de las "reglas del juego" (la lógica). Esto se logró dividiendo el código en dos paquetes principales:

* **Paquete `shapes` (Formas):** Se encarga de todo lo que el usuario ve en la pantalla. Contiene las figuras geométricas básicas como círculos, rectángulos y triángulos, y el lienzo (Canvas) donde se dibujan.
* **Paquete `tower` (Torre):** Contiene el cerebro del proyecto. Aquí viven las reglas de cómo se comportan las tazas y las tapas. 

**¿Por qué se eligió este diseño?**
Se eligió porque permite la **Reutilización y Extensibilidad**. Al usar conceptos de herencia (donde un objeto hereda características de otro), se creó una clase general llamada `StackingItem` (Elemento Apilable). De ella nacen la Taza (`Cup`) y la Tapa (`Lid`). Luego, de estas nacen versiones más locas y complejas (como la Tapa Miedosa o la Taza Jerárquica). 
Si el día de mañana se quiere inventar una "Taza Explosiva", solo se debe crear una nueva clase que herede las reglas básicas y agregarle su comportamiento especial, sin tener que reescribir todo el simulador. Además, los errores se controlan de forma centralizada mediante excepciones propias (`TowerException`).

---

## Funcionalidades
El simulador no es solo una calculadora, es un entorno interactivo. Sus características más relevantes son:

* **Gestión Manual de la Torre:** Permite al usuario crear torres, agregar o quitar tazas y tapas una por una.
* **Reorganización Inteligente:** Puede ordenar las piezas de mayor a menor, invertir el orden de la torre, o intercambiar piezas de posición.
* **Comportamientos Especiales:**
    * *Opener Cup (Taza Abridora):* Al entrar, destruye las tapas que le estorban.
    * *Hierarchical Cup (Taza Jerárquica):* Empuja hacia arriba a las tazas más pequeñas para abrirse paso hasta el fondo.
    * *Crazy Lid (Tapa Loca):* En lugar de ir arriba de su taza, se coloca debajo como base.
    * *Fearful Lid (Tapa Miedosa):* Solo entra a la torre si su taza compañera ya está adentro, y se niega a salir si la está protegiendo.
* **Cálculo de Alturas:** El simulador calcula la altura total dinámica de la torre teniendo en cuenta que las tapas miden 1 cm y que las tazas encajan unas dentro de otras dependiendo de su tamaño.
* **Solucionador de la Maratón:** Recibe una cantidad de tazas y una altura objetivo, e indica exactamente en qué orden deben apilarse para ganar el reto, mostrando la solución de forma visual.

---

## Por mejorar
Aunque el proyecto cumple con los requisitos más exigentes, siempre hay espacio para crecer. Algunas recomendaciones para quienes deseen extender este trabajo:

1. **Interfaz Gráfica Moderna (GUI):** Actualmente se usa un lienzo básico (`Canvas`). Mover el proyecto a una tecnología más moderna como JavaFX, o incluso conectarlo a una página web usando Spring Boot, haría que el simulador fuera mucho más atractivo e interactivo (permitiendo, por ejemplo, arrastrar y soltar las tazas con el mouse).
2. **Animaciones Fluidas:** En lugar de que las piezas aparezcan mágicamente en su lugar, se podrían programar animaciones que muestren cómo las tazas caen y las tapas rebotan o se acomodan según sus reglas especiales.
3. **Optimización para Datos Masivos:** Aunque el solucionador algorítmico es efectivo, si se le piden calcular torres con millones de tazas, podría tardar. Implementar técnicas avanzadas de programación dinámica o heurísticas podría hacer que encuentre las soluciones en milisegundos.
4. **Integración Continua (CI/CD):** Configurar GitHub Actions para que, cada vez que alguien suba código nuevo, el sistema corra automáticamente las pruebas con JaCoCo y PMD, asegurando que nadie rompa las reglas de calidad por accidente.
