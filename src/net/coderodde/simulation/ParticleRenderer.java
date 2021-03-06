package net.coderodde.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;
import static net.coderodde.simulation.Configuration.PIXELS_PER_UNIT_LENGTH;

/**
 * This class describes the way a particle is rendered.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Sep 6, 2017)
 */
public final class ParticleRenderer {

    private final Particle particle;
    private final Color color; 
    
    public ParticleRenderer(Particle particle, Color color) {
        this.particle = Objects.requireNonNull(particle, 
                                               "The input particle is null.");
        this.color = Objects.requireNonNull(color, "The input color is null.");
    }

    /**
     * Draws this particle on a canvas.
     * 
     * @param g the graphics context.
     */
    public void draw(Graphics g) {
        int effectiveX = (int)(particle.getX() * PIXELS_PER_UNIT_LENGTH);
        int effectiveY = (int)(particle.getY() * PIXELS_PER_UNIT_LENGTH);
        int radius = (int)(particle.getRadius() *  PIXELS_PER_UNIT_LENGTH);
        g.setColor(color);
        g.fillOval(effectiveX - radius, 
                   effectiveY - radius,
                   2 * radius,
                   2 * radius);
    }
}
