package io.improbable.keanu.vertices.dbl.nonprobabilistic.operators.unary;

import io.improbable.keanu.vertices.Vertex;
import io.improbable.keanu.vertices.dbl.DoubleVertex;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.ConstantDoubleVertex;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.diff.DualNumber;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.diff.PartialDerivatives;

import java.util.Map;

public class ArcTanVertex extends DoubleUnaryOpVertex {

    public ArcTanVertex(DoubleVertex inputVertex) {
        super(inputVertex);
    }

    public ArcTanVertex(double inputValue) {
        this(new ConstantDoubleVertex(inputValue));
    }

    @Override
    protected Double op(Double a) {
        return Math.atan(a);
    }

    @Override
    public DualNumber calculateDualNumber(Map<Vertex, DualNumber> dualNumbers) {
        DualNumber inputDualNumber = dualNumbers.get(inputVertex);
        double dArcTan = 1 / (1 + Math.pow(inputVertex.getValue(), 2));
        PartialDerivatives outputPartialDerivative = inputDualNumber.getPartialDerivatives().multiplyBy(dArcTan);
        return new DualNumber(op(inputVertex.getValue()), outputPartialDerivative);
    }
}
