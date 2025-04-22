# Pokémon PvP - Mecàniques

## Mecànica del joc

El jugador haurà de formar equips de Pokémon per lluitar contra altres equips i així:

- Desbloquejar nous Pokémon
- Millorar les seves estadístiques
- Pujar de nivell en la partida

L’historial de les partides quedarà registrat en una base de dades i es podrà consultar des d’una vista del joc.

## Mecànica de les batalles:

Per a cada batalla:

- S’escull el mapa on es desenvoluparà la lluita
- El jugador escull tres Pokémon de la seva col·lecció (només pot triar els desbloquejats)
- Escull un dels tres com a Pokémon actiu (el que realitzarà els atacs)

El combat és per torns:

- Comença el torn
- El jugador i la màquina escullen un atac
- El Pokémon amb més velocitat actua primer
- Es resolen els atacs i s’actualitzen les estadístiques (vida, atac, estamina)
- Si un Pokémon cau, s’escull un nou Pokémon actiu per continuar
- Quan els tres Pokémon d’un equip cauen (per falta de vida o estamina), la partida finalitza.

## Mecànica dels atacs:

Els atacs tenen tres efectes principals:

- Poden fallar segons una taula de probabilitats (afegeix aleatorietat)
- Consumeixen estamina del Pokémon que ataca:
    * Si l’estamina és inferior al 25%, el Pokémon pot desobeir el 25% de les vegades
    * Si l’estamina arriba a 0, el Pokémon queda fora de combat
- Treu vida del Pokémon rival si l’atac és encertat

Cada atac té definits els següents atributs:

- **Dany**: vida que treu a l’oponent
- **Tipus**: determina l’efectivitat segons el tipus del Pokémon rival
    * Multiplica ×2 si el tipus de l’atac és efectiu contra el tipus del rival
    * Divideix /2 si el tipus de l’atac és poc efectiu contra el rival
- **Estamina**: consumida pel Pokémon que fa l’atac

Aquesta és la taula de tipus d'atacs:

| Tipus       | Efectiu contra                  | Feble contra               |
|-------------|----------------------------------|----------------------------|
| **Foc**     | Plantes, Gel, Insectes, Acer     | Aigua, Roca, Foc            |
| **Aigua**   | Foc, Roca, Terra                 | Aigua, Plantes, Dracs       |
| **Elèctric**| Aigua, Volador                   | Terra, Plantes, Dracs       |
| **Gel**     | Plantes, Terra, Voladors, Dracs  | Foc, Aigua, Acer            |
| **Verí**    | Plantes, Fades                   | Terra, Fantasmes, Acer      |

Per exemple, un atac de tipus Foc amb dany base 40:

- Treurà 80 de vida si colpeja un Pokémon de tipus Planta
- Treurà 20 de vida si colpeja un Pokémon de tipus Roca

## Mecànica de ítems:

Al final d'una batalla s'aconsegueixen ítems que permeten millorar els Pokémons.

- Quan es perd una batalla, el 25% de les vegades et dóna un "X Attack" o bé un "X Deffense"

- Quan es guanya una batalla, el 50% de les vegades et dóna un "X Attack" o bé un "X Deffense"

- Quan es guanya una batalla, el 50% de les vegades et dóna un "Bottle Cap" independentment de si ja t'ha donat un "X Attack" o un "X Deffense"

## Puntuació i canvis de nivell

Després de cada atac, el jugador rep punts d’experiència:

- Si l’atac falla, s’aconsegueixen entre 50 i 100 punts.

- Si l’atac té èxit, s’aconsegueixen entre 100 i 200 punts.

El nivell de partida es determina pel total acumulat de punts. Cada 1000 punts s’assoleix un nou nivell.

Per exemple:

Amb 2345 punts, el jugador té nivell 2, ja que 2345 / 1000 = 2 (ignorant decimals).

Quan el jugador puja de nivell, desbloqueja 2 nous Pokémon de la llista de bloquejats.