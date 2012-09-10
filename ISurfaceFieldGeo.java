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
   3D vector filed defined by a NURBS surface.
   
   @author Satoru Sugihara
*/

public class ISurfaceFieldGeo extends IFieldGeo implements I3DFieldI{ //extends I3DFieldBase{
    
    public ISurfaceI surface;
    public ISurfaceI fieldSurface;
    
    public ISurfaceFieldGeo(ISurfaceI srf, ISurfaceI fieldSrf){ surface = srf; fieldSurface = fieldSrf; }
    
    
    /** get original field value out of curve parameter u */
    public IVecI get(IVecI v, IVec2I uv){ return fieldSurface.pt(uv); }
    
    /** get 3D vector field value */
    public IVecI get(IVecI v){
	IVec2I uv = surface.uv(v);
	double r = intensity;
	if(decay == Decay.Linear){
	    double dist = surface.pt(uv).dist(v);
	    if(dist >= threshold) return new IVec(); // zero
	    if(threshold>0) r *= (threshold-dist)/threshold;
	}
	else if(decay == Decay.Gaussian){
	    double dist = surface.pt(uv).dist(v);
	    if(threshold>0) r *= Math.exp(-2*dist*dist/(threshold*threshold));
	}
	
	IVecI vec = get(v,uv);
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
		IVecI vec = get(v,surface.uv(v));
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity);
	    }
	    return get(v,surface.uv(v)).mul(intensity); // need get() in case of ISurfaceR ?
	} 
	case Linear:{
	    IVec2I uv = surface.uv(v);
	    double dist = surface.pt(uv).dist(v);
	    if(dist >= threshold) return new IVec(); // zero
	    if(constantIntensity){
		IVecI vec = get(v,uv);
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity*dist/threshold);
	    }
	    return get(v,uv).mul(intensity*dist/threshold);
	}
	case Gaussian:{
	    IVec2I uv = surface.uv(v);
	    double dist = surface.pt(uv).dist(v);
	    if(constantIntensity){
		IVecI vec = get(v,uv);
		double len = vec.len();
		if(len<IConfig.tolerance){ return vec.zero(); }
		return vec.len(intensity*Math.exp(-2*dist*dist/(threshold*threshold)));
	    }
	    return get(v,uv).mul(intensity*Math.exp(-2*dist*dist/(threshold*threshold)));
	}
	}
	return null;
	*/
    }
    
    /** if output vector is besed on constant length (intensity) or variable depending geometry when curve or surface tangent is used */
    public ISurfaceFieldGeo constantIntensity(boolean b){ super.constantIntensity(b); return this; }
    
    /** set no decay */
    public ISurfaceFieldGeo noDecay(){ super.noDecay(); return this; }
    /** set linear decay; When distance is equal to threshold, output is zero.*/
    public ISurfaceFieldGeo linearDecay(double threshold){
	super.linearDecay(threshold); return this;
    }
    public ISurfaceFieldGeo linear(double threshold){
	super.linear(threshold); return this;
    }
    /** set Gaussian decay; Threshold is used as double of standard deviation (when distance is eqaul to threshold, output is 13.5% of original).
     */
    public ISurfaceFieldGeo gaussianDecay(double threshold){
	super.gaussianDecay(threshold); return this;
    }
    public ISurfaceFieldGeo gaussian(double threshold){
	super.gaussian(threshold); return this;
    }
    public ISurfaceFieldGeo threshold(double t){ super.threshold(t); return this; }
    public ISurfaceFieldGeo intensity(double i){ super.intensity(i); return this; }
    
}
