/*---

    iGeo - http://igeo.jp

    Copyright (c) 2002-2012 Satoru Sugihara

    This file is part of iGeo.

    iGeo is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, version 3.

    iGeo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with iGeo.  If not, see <http://www.gnu.org/licenses/>.

---*/

package igeo;

/**
   2D vector filed defined by a NURBS curve.
   
   @author Satoru Sugihara
*/

public class I2DCurveFieldGeo extends IFieldGeo implements I2DFieldI{
    
    public ICurveI curve;
    public ICurveI fieldCurve;
    
    public I2DCurveFieldGeo(ICurveI crv, ICurveI fieldCrv){ curve = crv; fieldCurve = fieldCrv; }
        
    /** get original field value out of curve parameter u */
    public IVec2I get(IVecI v, double u){ return fieldCurve.pt(u).to2d(); }
    
    /** get 3D vector field value */
    public IVec2I get(IVecI v){
	double u = curve.u(v.to2d());
	double r = intensity;
	if(decay == Decay.Linear){
	    double dist = curve.pt(u).to2d().dist(v.to2d());
	    if(dist >= threshold) return new IVec2(); // zero
	    if(threshold>0) r *= (threshold-dist)/threshold;
	}
	else if(decay == Decay.Gaussian){
	    double dist = curve.pt(u).to2d().dist(v.to2d());
	    if(threshold>0) r *= Math.exp(-2*dist*dist/(threshold*threshold));
	}
	
	IVec2I vec = get(v,u);
	if(constantIntensity){
	    double len = vec.len();
	    if(len<IConfig.tolerance){ return vec.zero(); }
	    return vec.len(r);
	}
	return vec.mul(r);
	
	/*
	switch(decay){
	case None:{
	    if(constantIntensity){
		IVec2I vec = get(v,curve.u(v.to2d()));
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity);
	    }
	    return get(v,curve.u(v.to2d())).mul(intensity); // variable intensity
	}
	case Linear:{
	    double u = curve.u(v.to2d());
	    double dist = curve.pt(u).to2d().dist(v.to2d());
	    if(dist >= threshold) return new IVec2(); // zero
	    if(constantIntensity){
		IVec2I vec = get(v,u);
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity*dist/threshold);
	    }
	    return get(v,u).mul(intensity*dist/threshold);
	}
	case Gaussian:{
	    double u = curve.u(v.to2d());
	    double dist = curve.pt(u).to2d().dist(v.to2d());
	    if(constantIntensity){
		IVec2I vec = get(v,u);
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity*Math.exp(-2*dist*dist/(threshold*threshold)));
	    }
	    return get(v,u).mul(intensity*Math.exp(-2*dist*dist/(threshold*threshold)));
	}
	}
	return new IVec2(); // should not reach here.
	*/
    }
    
    /** if output vector is besed on constant length (intensity) or variable depending geometry when curve or surface tangent is used */
    public I2DCurveFieldGeo constantIntensity(boolean b){ super.constantIntensity(b); return this; }
    
    /** set no decay */
    public I2DCurveFieldGeo noDecay(){ super.noDecay(); return this; }
    /** set linear decay; When distance is equal to threshold, output is zero.*/
    public I2DCurveFieldGeo linearDecay(double threshold){
	super.linearDecay(threshold); return this;
    }
    public I2DCurveFieldGeo linear(double threshold){ super.linear(threshold); return this; }
    
    /** set Gaussian decay; Threshold is used as double of standard deviation (when distance is eqaul to threshold, output is 13.5% of original).
     */
    public I2DCurveFieldGeo gaussianDecay(double threshold){
	super.gaussianDecay(threshold); return this;
    }
    public I2DCurveFieldGeo gaussian(double threshold){ super.gaussian(threshold); return this; }
    
    public I2DCurveFieldGeo threshold(double t){ super.threshold(t); return this; }
    public I2DCurveFieldGeo intensity(double i){ super.intensity(i); return this; }
    
}
