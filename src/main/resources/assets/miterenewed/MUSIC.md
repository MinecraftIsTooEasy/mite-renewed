## music.json specification
Music entries are defined based on asset namespace and filename. The definition is only able to utilize an
already defined namespace, such as ``minecraft`` or ``miterenewed``. Entries must have the ``.ogg`` file suffix
included.

Invalid entry key: ``custom:jamz.ogg``
Invalid entry key: ``minecraft:nuance2``
Valid entry key: ``minecraft:calm1.ogg``

Inside the music entry, you can define some metadata:
```json
{
  "minecraft:calm1.ogg": {
    "title": "Minecraft",
    "artist": "C418",
    "conditions" : {
      "miterenewed:dimension": {
        "dimension": "overworld"
      },
      "miterenewed:time": {
        "from" : 0,
        "to" : 14000
      }
    }
  }
}
```
The definition consists of two major parts: the music's metadata, and the music's conditions in which
it's able to play. It's recommended to at least provide the ``title`` and ``artist`` fields for the 
in-game track display.

Posisble metadata fields:
- ``title`` - Title of the track
- ``artist`` - Artist attributed for making the track
- ``prevent_pitching`` - Prevents this music from having its pitch altered
- ``hide_display`` - Prevents this music from showing the track display

The music engine is based on context provided by the world and your player. Most tracks built into
MiTE Renewed only utilize dimension and time conditions, but a full list of possible conditions and
their arguments are as follows:
- ``miterenewed:generic`` - Internal condition, assigned if there are no conditions provided
- ``miterenewed:title`` - Only allows this music to play on the titlescreen
- ``miterenewed:dimension`` - Only allows this music to play in a certain dimension
  - ``dimension`` - Name of the dimension, lowecase. e.g. ``overworld``, ``underworld``
- ``miterenewed:time`` - Only allows this music to play during a certain timeframe, measured in ticks
  - ``from`` - Start time period
  - ``to`` - End time period
- ``miterenewed:height`` - Only allows this music to play when the player is between two y-levels
    - ``from`` - Lowest acceptable height
    - ``to`` - Highest acceptable height
- ``miterenewed:humidity`` - Only allows this music to play when the player is within a biome temperature range
    - ``from`` - Lowest acceptable temperature
    - ``to`` - Highest acceptable temperature
- ``miterenewed:event`` - Only allows this music to play during an event
  - ``blood_moon`` - Set to ``true`` if the music plays during the blood moon
  - ``harvest_moon`` - Set to ``true`` if the music plays during the harvest moon
  - ``blue_moon`` - Set to ``true`` if the music plays during the blue moon
  - ``rain`` - Set to ``true`` if the music plays during rainfall
  - ``thunder`` - Set to ``true`` if the music plays when its thundering
