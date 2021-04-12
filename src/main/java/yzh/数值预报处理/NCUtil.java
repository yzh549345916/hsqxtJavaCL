package yzh.数值预报处理;

import ucar.nc2.Attribute;

public class NCUtil {
    /**
     * Convert from netcdf dimension to meteothink dimension
     * @param ncDim Netcdf dimension
     * @return MeteoThink dimension
     */
    public static Dimension convertDimension(ucar.nc2.Dimension ncDim) {
        Dimension dim = new Dimension();
        dim.setShortName(ncDim.getShortName());
        dim.setLength(ncDim.getLength());
        dim.setUnlimited(ncDim.isUnlimited());
        dim.setShared(ncDim.isShared());
        dim.setVariableLength(ncDim.isVariableLength());

        return dim;
    }
    /**
     * Convert netcdf attribute to meteothink attribute
     * @param ncAttr Netcdf attribute
     * @return MeteoThink attribute
     */
    public static Attribute convertAttribute(ucar.nc2.Attribute ncAttr) {


        return ncAttr;
    }
}
