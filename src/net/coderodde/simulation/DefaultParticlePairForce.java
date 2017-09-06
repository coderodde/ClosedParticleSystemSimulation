package net.coderodde.simulation;

import static net.coderodde.simulation.Configuration.FORCE_CONSTANT;

public final class DefaultParticlePairForce implements ParticlePairForce {

    @Override
    public double getForce(Particle particle1, Particle particle2) {
        double distance = particle1.getDistance(particle2);
        double mass1 = particle1.getMass();
        double mass2 = particle2.getMass();
        return FORCE_CONSTANT * mass1 * mass2 / (distance * distance);
    }
}
