package io.improbable.keanu.vertices.dbl.probabilistic;

import io.improbable.keanu.distributions.continuous.Gaussian;
import io.improbable.keanu.vertices.dbl.DoubleVertex;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.ConstantDoubleVertex;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.diff.PartialDerivatives;

import java.util.Map;
import java.util.Random;

public class GaussianVertex extends ProbabilisticDouble {

    private final DoubleVertex mu;
    private final DoubleVertex sigma;
    private final Random random;

    public GaussianVertex(DoubleVertex mu, DoubleVertex sigma, Random random) {
        this.mu = mu;
        this.sigma = sigma;
        this.random = random;
        setParents(mu, sigma);
    }

    public GaussianVertex(DoubleVertex mu, double sigma, Random random) {
        this(mu, new ConstantDoubleVertex(sigma), random);
    }

    public GaussianVertex(double mu, DoubleVertex sigma, Random random) {
        this(new ConstantDoubleVertex(mu), sigma, random);
    }

    public GaussianVertex(double mu, double sigma, Random random) {
        this(new ConstantDoubleVertex(mu), new ConstantDoubleVertex(sigma), random);
    }

    public GaussianVertex(DoubleVertex mu, DoubleVertex sigma) {
        this(mu, sigma, new Random());
    }

    public GaussianVertex(double mu, double sigma) {
        this(new ConstantDoubleVertex(mu), new ConstantDoubleVertex(sigma), new Random());
    }

    public GaussianVertex(double mu, DoubleVertex sigma) {
        this(new ConstantDoubleVertex(mu), sigma, new Random());
    }

    public GaussianVertex(DoubleVertex mu, double sigma) {
        this(mu, new ConstantDoubleVertex(sigma), new Random());
    }


    public DoubleVertex getMu() {
        return mu;
    }

    public DoubleVertex getSigma() {
        return sigma;
    }

    @Override
    public double logPdf(Double value) {
        return Gaussian.logPdf(mu.getValue(), sigma.getValue(), value);
    }

    @Override
    public Map<String, Double> dLogPdf(Double value) {
        Gaussian.Diff dlnP = Gaussian.dlnPdf(mu.getValue(), sigma.getValue(), value);
        return convertDualNumbersToDiff(dlnP.dPdmu, dlnP.dPdsigma, dlnP.dPdx);
    }

    private Map<String, Double> convertDualNumbersToDiff(double dPdmu, double dPdsigma, double dPdx) {
        PartialDerivatives dPdInputsFromMu = mu.getDualNumber().getPartialDerivatives().multiplyBy(dPdmu);
        PartialDerivatives dPdInputsFromSigma = sigma.getDualNumber().getPartialDerivatives().multiplyBy(dPdsigma);
        PartialDerivatives dPdInputs = dPdInputsFromMu.add(dPdInputsFromSigma);

        if (!isObserved()) {
            dPdInputs.putWithRespectTo(getId(), dPdx);
        }

        return dPdInputs.asMap();
    }

    @Override
    public Double sample() {
        return Gaussian.sample(mu.getValue(), sigma.getValue(), random);
    }

}
