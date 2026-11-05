# Taller: un Proxy para migrar sin salir de `main`

Migración del cálculo de envío de una tienda usando **Trunk-Based Development** y el patrón **Proxy**. Todo el trabajo se hizo en una sola rama (`main`), con commits pequeños que dejan los tests en verde. El código nuevo entró apagado detrás de un Proxy con un feature flag y, al final, se encendió y se limpió.

- **Lenguaje:** Java 17
- **Pruebas:** JUnit 5
- **Build:** Maven

## Cómo correr los tests

```bash
mvn test
```

En NetBeans: clic derecho sobre el proyecto → **Test** (o `Alt + F6`).

## Interfaz gráfica

`ShippingApp` es una ventana Swing (sin dependencias extra) que recalcula el envío y el total **mientras se escribe**: elige la ciudad, el peso y el subtotal, y muestra el desglose (peso × 2,5, recargo fuera de Bogotá, envío y total). No tiene reglas de negocio propias: usa `Checkout` a través de `ShopConfig`, así que cualquier cambio futuro en el cálculo se refleja solo en la pantalla.

Para abrirla:

- **NetBeans:** clic derecho sobre `ShippingApp.java` → **Run File** (`Shift + F6`).
- **Terminal:**

  ```bash
  mvn compile
  java -cp target/classes shop.ShippingApp
  ```

## Estado final del código

```
src/main/java/shop/
├── Order.java                    record con los datos del pedido
├── ShippingCalculator.java       interfaz que usa el cliente
├── SmartShippingCalculator.java  cálculo nuevo (2,5 por kilo, +5 fuera de Bogotá)
├── Checkout.java                 recibe un ShippingCalculator por constructor
├── ShopConfig.java               arma el Checkout con el cálculo nuevo
└── ShippingApp.java              interfaz gráfica (Swing)
```

## Dónde está el Proxy

El Proxy y el flag existieron durante la migración y se **eliminaron a propósito en el paso 6**, porque un flag que nunca se borra se vuelve deuda técnica. Para verlos, hay que revisar el historial:

| Qué buscar | Commit |
|---|---|
| Proxy con el flag apagado | `4fead52` |
| Tests de los dos caminos del flag | `97c5eb3` |
| Extensión del Proxy (canary) | `3fc3704` |

Para ver el código tal como estaba en un commit:

```bash
git show 4fead52
git checkout 3fc3704      # estado completo del proyecto en ese punto
git checkout main         # volver al estado final
```

## Historial de commits

| Paso | Commit | Mensaje |
|---|---|---|
| 0 | `7f894d0` | `chore: initial shop with legacy shipping` |
| 1 | `f40fb3c` | `refactor: extract ShippingCalculator interface` |
| 2 | `ca8fbc3` | `refactor: inject ShippingCalculator into Checkout` |
| 3 | `4fead52` | `feat: add ShippingCalculatorProxy (flag off)` |
| 4 | `5ea715b` | `feat: smart shipping by weight` |
| 4 | `7702ae8` | `feat: smart shipping surcharge outside Bogotá` |
| 4 | `97c5eb3` | `test: cover both paths of smart-shipping flag` |
| 5 | `3fc3704` | `feat: proxy canary` |
| 6 | `46baa1b` | `chore: remove canary, roll out to all customers` |
| 6 | `8dd7eb6` | `chore: enable smart-shipping` |
| 6 | `864779b` | `chore: remove legacy shipping and flag` |

Los commits `97c5eb3` y `46baa1b` no figuran en la lista de mensajes del taller. Se agregaron para mantener los cambios pequeños: el primero completa los tests del Proxy y el segundo quita el canary antes de encender el flag para todos los clientes.

## Decisiones de diseño

- **Interfaz primero (pasos 1 y 2).** `Checkout` pasó a depender de `ShippingCalculator`, no de una clase concreta. Gracias a eso el Proxy se pudo insertar sin tocar al cliente.
- **Código nuevo apagado (pasos 3 y 4).** `SmartShippingCalculator` se construyó regla por regla, con su test, mientras el flag `smart-shipping` estaba apagado y nadie lo usaba.
- **Canary (paso 5).** Con el flag encendido, solo los clientes con `Math.abs(customerId.hashCode()) % 100 < 20` usaban el cálculo nuevo.
- **Limpieza (paso 6).** Se quitó el canary, se encendió el flag para todos y se borraron el calculador viejo, el flag y el Proxy.
