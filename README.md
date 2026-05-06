# 🌌 jGravity — N-Body Gravity Simulator in Java (2-D only for now, 3-D to come soon)

A real-time 2D gravity simulator built with Java Swing, featuring N-body physics, inelastic collisions, a spacetime curvature grid, motion trails, and a fully interactive UI.

> Inspired by and functionally equivalent to the C++ OpenGL gravity simulator built by kavan010 — but built entirely in Java from scratch.
> Here's his YouTube video for reference : https://www.youtube.com/watch?v=_YbGWoUaZg0&pp=ygUWZ3Jhdml0eSBzaW11bGF0aW9uIGMrKw%3D%3D

***

## 📸 Features

- **N-body gravitational physics** — every body attracts every other body using Newton's law of universal gravitation
- **Inelastic collisions** — bodies merge on contact with momentum conservation
- **Spacetime grid** — Flamm's paraboloid warp visualises gravitational curvature in real time
- **Motion trails** — circular trail buffer traces each body's path
- **Live HUD** — displays body count, total kinetic energy, and paused state
- **Solar system opening scene** — stable circular orbits calculated from vis-viva equation
- **Mouse spawner** — click and drag to spawn a body; drag length and direction set the velocity vector, with a live preview line
- **Keyboard controls** — full keyboard interface (see Controls below)
- **Antialiased rendering** — shape and text antialiasing via `RenderingHints`

***

## 🎮 Controls

| Key / Input | Action |
|---|---|
| `SPACE` | Pause / resume simulation |
| `R` | Reset to opening solar system scene |
| `C` | Clear all bodies |
| `H` | Toggle help overlay |
| Left click + drag | Spawn body — drag direction = velocity |
| Right click | Remove nearest body |

***

## 🧠 Physics

### Gravitational Force
Newton's law of universal gravitation with a softening factor to prevent singularities at close range:

$$ F = \frac{G \cdot m_1 \cdot m_2}{d^2 + \epsilon^2} $$

where $$\epsilon$$ (`SOFTENING`) prevents infinite forces when bodies are very close.

### Integration
Euler integration steps each body forward by timestep `dt` each frame:

$$ v \leftarrow v + a \cdot dt $$
$$ x \leftarrow x + v \cdot dt $$

### Collisions
When the distance between two bodies is less than the sum of their radii, they merge inelastically. The resulting body conserves momentum:

$$ v_{out} = \frac{m_1 v_1 + m_2 v_2}{m_1 + m_2} $$

Mass and radius of the merged body are summed.

### Stable Orbit Velocity
The opening solar system scene initialises each planet's tangential velocity using the vis-viva equation for a circular orbit:

$$ v = \sqrt{\frac{G \cdot M}{r}} $$

***

## 🗂️ Project Structure

```
src/
└── main/java/rottenambrosia/gravitysim/
    ├── Main.java              — entry point, creates JFrame + SimulationPanel
    ├── SimulationPanel.java   — Swing panel, game loop, rendering, input wiring
    ├── PhysicsEngine.java     — gravity, integration, collision detection
    ├── Body.java              — position, velocity, mass, radius, trail buffer
    ├── MouseInteraction.java  — mouse spawn + remove (MouseListener + MouseMotionListener)
    ├── SpacetimeGrid.java     — Flamm's paraboloid warp grid renderer
    └── Constants.java         — G, dt, SOFTENING, SPAWN_MASS, SPAWN_RADIUS, VELOCITY_SCALE
```

***

## 🔧 Building & Running

### Requirements

- Java 21+

### Run

```bash
mvn compile exec:java -Dexec.mainClass="rottenambrosia.gravitysim.Main"
```

Or open in IntelliJ IDEA and run `Main.java` directly.

***

## 📐 Constants

| Constant | Default | Description |
|---|---|---|
| `G` | `6.674e-3` | Gravitational constant (scaled for screen units) |
| `dt` | `0.5` | Physics timestep per frame |
| `SOFTENING` | `15.0` | Singularity softening factor |
| `SPAWN_MASS` | `1.0e4` | Default mass of spawned bodies |
| `SPAWN_RADIUS` | `10.0` | Default radius of spawned bodies |
| `VELOCITY_SCALE` | `0.15` | Scales drag distance to initial velocity (Feel free to change this to your liking)|

***

## 📜 License

MIT
