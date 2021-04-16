package yzh.数值预报处理;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;
import ucar.nc2.iosp.hdf5.H5header;
import ucar.unidata.geoloc.EarthEllipsoid;
import ucar.unidata.io.RandomAccessFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NetCDFDataInfo {
    //是否为京津冀数据的标识
    public Boolean _jingjinjiBS = Boolean.FALSE;
    private String _fileTypeStr;
    private String _fileTypeId;
    private Conventions _convention = Conventions.CF;
    private String _fileName;
    private NetcdfFile ncfile = null;
    protected List<Variable> ncVariables = new ArrayList<>();
    private List<ucar.nc2.Dimension> ncDimensions = new ArrayList<>();
    private List<ucar.nc2.Attribute> ncAttributes = new ArrayList<>();
    private boolean keepOpen = false;
    private boolean _isHDFEOS = false;
    private boolean _isSWATH = false;
    private boolean _isPROFILE = false;
    protected yzh.数值预报处理.Dimension tDim = null;
    protected yzh.数值预报处理.Dimension xDim = null;
    protected yzh.数值预报处理.Dimension yDim = null;
    protected yzh.数值预报处理.Dimension zDim = null;

    public NetCDFDataInfo(String fileName) {
        _fileName = fileName;
    }

    public NetCDFDataInfo(String fileName, boolean bs) {
        _fileName = fileName;
        _jingjinjiBS = bs;
    }

    /**
     * Test if the file can be opened.
     *
     * @param fileName The file name.
     * @return Can be opened or not.
     * @throws IOException
     */
    public static boolean canOpen(String fileName) throws IOException {
        boolean r = NetcdfFiles.canOpen(fileName);
        if (!r) {
            RandomAccessFile raf = RandomAccessFile.acquire(fileName);
            r = H5header.isValidFile(raf);
        }
        return r;
    }

    public void readDataInfo(String fileName) {
        _fileName = fileName;
        try {
            //ncfile = NetcdfFile.open(fileName);
            //ncfile = NetcdfDatasets.openDataset(fileName);
            ncfile = NetcdfFiles.open(fileName);
            readDataInfo(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readDataInfo() {
        try {
            //ncfile = NetcdfFile.open(fileName);
            //ncfile = NetcdfDatasets.openDataset(fileName);
            ncfile = NetcdfFiles.open(_fileName);
            readDataInfo(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readDataInfo(boolean keepOpen) {
        try {
            _fileTypeStr = ncfile.getFileTypeDescription();
            _fileTypeId = ncfile.getFileTypeId();
            ncVariables = ncfile.getVariables();
            NetcdfFile.Builder buil = ncfile.toBuilder();
            //Read dimensions
            Iterable<ucar.nc2.Dimension> mydim = buil.rootGroup.getDimensions();
            ncDimensions.clear();
            mydim.forEach(single -> {
                ncDimensions.add(single);
            });
            ncAttributes = ncfile.getGlobalAttributes();
            String featureType = this.getGlobalAttStr("featureType");
            switch (featureType) {
                case "SWATH":
                    _isSWATH = true;
                    break;
                case "PROFILE":
                    _isPROFILE = true;
                    break;
            }
            //Get convention
            _convention = this.getConvention();
            EarthEllipsoid pro = EarthEllipsoid.WGS84;
            try {
                getDimValues_CF();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            //getVariableLevels();
        } finally {
            /*this.keepOpen = keepOpen;
            if (!keepOpen) {
                if (null != ncfile) {
                    try {
                        ncfile.close();
                        ncfile = null;
                    } catch (IOException ex) {
                        Logger.getLogger(NetCDFDataInfo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }*/
        }
        //this.keepOpen = true;
    }

    private void getDimValues_CF() throws IOException {
        for (ucar.nc2.Variable var : ncVariables) {
            if (var.getRank() == 1) {
                if (!var.getDataType().isNumeric()) {
                    continue;
                }
                DimensionType dimType = getDimType(var);
                Array values = var.read();
                Dimension v2 = var.getDimension(0);
                yzh.数值预报处理.Dimension dim = new yzh.数值预报处理.Dimension();
                dim.setDimType(dimType);
                dim.setShortName(v2.getShortName());
                if (!_jingjinjiBS) {
                    switch (dimType) {
                        case X:
                            dim.setValues((float[]) values.copyToNDJavaArray());
                            dim.setUnits(var.findAttributeString("units", ""));
                            dim.setStep(dim.getValues()[1] - dim.getValues()[0]);
                            xDim = dim;
                            break;
                        case Y:
                            dim.setValues((float[]) values.copyToNDJavaArray());
                            dim.setUnits(var.findAttributeString("units", ""));
                            dim.setStep(dim.getValues()[1] - dim.getValues()[0]);
                            yDim = dim;
                            break;
                        case Z:
                           try{
                               DataType dataType=var.getDataType();
                               if(dataType.isNumeric()){
                                   if(dataType.isIntegral()){
                                       dim.setValues( int转double((int[]) values.copyToNDJavaArray()));
                                   }else {
                                       dim.setValues((float[]) values.copyToNDJavaArray());
                                   }
                               }

                           } catch (Exception e) {


                           }
                            dim.setUnits(var.findAttributeString("units", ""));
                            zDim = dim;
                            break;
                        case T:
                            List<LocalDateTime> times = this.getTimes(var, values);
                            if (times != null) {
                                List<Double> ts = new ArrayList<>();
                                for (LocalDateTime t : times) {

                                    ts.add((double) t.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                                }
                                dim.setValues(ts);
                                tDim = dim;
                            }
                            break;
                        default:

                            break;
                    }
                } else {
                    switch (dimType) {
                        case X:
                            dim.setValues(数据批量四舍五入((double[]) values.copyToNDJavaArray()));
                            dim.setUnits(var.findAttributeString("units", ""));
                            dim.setStep(NumberUtil.round(dim.getValues()[1] - dim.getValues()[0], 6).doubleValue());
                            xDim = dim;
                            break;
                        case Y:
                            dim.setValues(数据批量四舍五入((double[]) values.copyToNDJavaArray()));
                            dim.setUnits(var.findAttributeString("units", ""));
                            dim.setStep(NumberUtil.round(dim.getValues()[1] - dim.getValues()[0], 6).doubleValue());
                            yDim = dim;
                            break;
                        case Z:
                            dim.setValues((float[]) values.copyToNDJavaArray());
                            dim.setUnits(var.findAttributeString("units", ""));
                            zDim = dim;
                            break;
                        case T:
                            dim.setValues(数据批量转换((long[]) values.copyToNDJavaArray()));
                            tDim = dim;
                            break;
                        default:
                            break;
                    }
                }

            }
        }
    }
    private double[] getPackData(ucar.nc2.Variable var) {
        double add_offset, scale_factor, missingValue = -9999.0;
        add_offset = 0;
        scale_factor = 1;
        for (int i = 0; i < var.getAttributes().size(); i++) {
            ucar.nc2.Attribute att = var.getAttributes().get(i);
            String attName = att.getShortName();
            if (attName.equals("add_offset")) {
                add_offset = Double.parseDouble(att.getValue(0).toString());
            }

            if (attName.equals("scale_factor")) {
                scale_factor = Double.parseDouble(att.getValue(0).toString());
            }

            if (attName.equals("missing_value")) {
                missingValue = Double.parseDouble(att.getValue(0).toString());
            }

            //MODIS NetCDF data
            if (attName.equals("_FillValue")) {
                try {
                    missingValue = Double.parseDouble(att.getValue(0).toString());
                } catch (NumberFormatException e) {

                }
            }
        }

//        //Adjust undefine data
//        if (Double.isNaN(missingValue)) {
//            missingValue = this.getMissingValue();
//        } else {
//            missingValue = missingValue * scale_factor + add_offset;
//        }
        return new double[]{add_offset, scale_factor, missingValue};
    }
    private double[] 数据批量四舍五入(double[] numbers) {
        double[] data = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            data[i] = NumberUtil.round(numbers[i], 6).doubleValue();
        }
        return data;
    }
    private double[] int转double(int[] szIn){
        double[] szout=new double[szIn.length];
        for(int i=0;i<szIn.length;i++){
            szout[i]=Double.valueOf(szIn[i]);
        }
        return szout;
    }
    private double[] 数据批量转换(long[] numbers) {
        double[] data = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            data[i] = numbers[i];
        }
        return data;
    }

    public void close() {
        try {
            ncfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LocalDateTime> getTimes(ucar.nc2.Variable aVar, Array values) {
        //Get start time
        String unitsStr;
        int i;
        List<LocalDateTime> times = new ArrayList<>();
        ucar.nc2.Attribute unitAtt = aVar.findAttribute("units");
        if (unitAtt == null) {
            /*LocalDateTime sTime = LocalDateTime.of(1985, 1, 1, 0, 0);
            IndexIterator ii = values.getIndexIterator();
            while (ii.hasNext()) {
                times.add(sTime.plusHours(ii.getIntNext()));
            }*/
            return times;
        }

        unitsStr = unitAtt.getStringValue();
        if (unitsStr.contains("as")) {
            if (unitsStr.contains("%")) {
                return null;
            }
            //Get data time
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
            for (i = 0; i < values.getSize(); i++) {
                String md = String.valueOf(values.getInt(i));
                if (md.length() <= 3) {
                    md = "0" + md;
                }
                times.add(LocalDateTime.parse(md, format));
            }
        } else {
            TimeUnit aTU = null;
            LocalDateTime sTime = LocalDateTime.now();
            if (unitsStr.equalsIgnoreCase("month")) {
                sTime = LocalDateTime.of(sTime.getYear(), 1, 1, 0, 0, 0);
                aTU = TimeUnit.Month;
            } else {
                aTU = this.getTimeUnit(unitsStr);
                sTime = this.getStartTime(unitsStr);
            }
            //getPSDTimeInfo(unitsStr, sTime, aTU);

            //Get data time
            for (i = 0; i < values.getSize(); i++) {
                switch (aTU) {
                    case Year:
                        times.add(sTime.plusYears(values.getInt(i)));
                        break;
                    case Month:
//                        if (unitsStr.equalsIgnoreCase("month")) {
//                            cal.add(Calendar.MONTH, values.getInt(i) - 1);
//                        } else {
//                            cal.add(Calendar.MONTH, values.getInt(i));
//                        }
                        times.add(sTime.plusMonths(values.getInt(i)));
                        break;
                    case Day:
                        times.add(sTime.plusDays(values.getInt(i)));
                        break;
                    case Hour:
                        if (sTime.getYear() == 1 && sTime.getMonthValue() == 1
                                && sTime.getDayOfMonth() == 1 && values.getInt(i) > 48) {
                            times.add(sTime.plusHours(values.getInt(i) - 48));
                        } else {
                            times.add(sTime.plusHours(values.getInt(i)));
                        }
                        break;
                    case Minute:
                        times.add(sTime.plusMinutes(values.getInt(i)));
                        break;
                    case Second:
                        times.add(sTime.plusSeconds(values.getInt(i)));
                        break;
                }
            }
        }

        return times;
    }

    private TimeUnit getTimeUnit(String tStr) {
        TimeUnit aTU = TimeUnit.Second;
        tStr = tStr.trim();
        String tu;
        String[] dataArray;
        int i;

        dataArray = tStr.split("\\s+");

        if (dataArray.length < 2) {
            return aTU;
        }

        //Get time unit
        tu = dataArray[0];
        if (tu.toLowerCase().contains("second")) {
            aTU = TimeUnit.Second;
        } else if (tu.length() == 1) {
            String str = tu.toLowerCase();
            switch (str) {
                case "y":
                    aTU = TimeUnit.Year;
                    break;
                case "d":
                    aTU = TimeUnit.Day;
                    break;
                case "h":
                    aTU = TimeUnit.Hour;
                    break;
                case "s":
                    aTU = TimeUnit.Second;
                    break;
            }
        } else {
            String str = tu.toLowerCase().substring(0, 2);
            switch (str) {
                case "yr":
                case "ye":
                    aTU = TimeUnit.Year;
                    break;
                case "mo":
                    aTU = TimeUnit.Month;
                    break;
                case "da":
                    aTU = TimeUnit.Day;
                    break;
                case "hr":
                case "ho":
                    aTU = TimeUnit.Hour;
                    break;
                case "mi":
                    aTU = TimeUnit.Minute;
                    break;
                case "se":
                    aTU = TimeUnit.Second;
                    break;
            }
        }

        return aTU;
    }

    private LocalDateTime getStartTime(String tStr) {
        LocalDateTime sTime = LocalDateTime.now();
        tStr = tStr.trim();
        String[] dataArray;

        dataArray = tStr.split("\\s+");

        if (dataArray.length < 3) {
            return sTime;
        }

        //Get start time
        String ST;
        ST = dataArray[2];
        if (ST.contains("T")) {
            dataArray = Arrays.copyOf(dataArray, dataArray.length + 1);
            dataArray[dataArray.length - 1] = ST.split("T")[1];
            ST = ST.split("T")[0];
        }
        int year = 2000, month = 1, day = 1;
        int hour = 0, min = 0, sec = 0;
        if (ST.contains("-")) {
            String[] darray1 = ST.split("-");
            year = Integer.parseInt(darray1[0]);
            month = Integer.parseInt(darray1[1]);
            if (darray1[2].length() > 2) {
                darray1[2] = darray1[2].substring(0, 2);
            }
            day = Integer.parseInt(darray1[2]);
            if (dataArray.length >= 4) {
                String hmsStr = dataArray[3];
                hmsStr = hmsStr.replace("0.0", "00");
                try {
                    String[] hms = hmsStr.split(":");
                    hour = Integer.parseInt(hms[0]);
                    if (hms.length > 1) {
                        min = Integer.parseInt(hms[1]);
                    }
                    if (hms.length > 2) {
                        sec = Integer.parseInt(hms[2]);
                    }
                } catch (NumberFormatException e) {
                }
            }
        } else if (ST.contains(":")) {
            String hmsStr = ST;
            hmsStr = hmsStr.replace("0.0", "00");
            try {
                String[] hms = hmsStr.split(":");
                hour = Integer.parseInt(hms[0]);
                if (hms.length > 1) {
                    min = Integer.parseInt(hms[1]);
                }
                if (hms.length > 2) {
                    sec = Integer.parseInt(hms[2]);
                }
            } catch (Exception e) {
            }
        }

        if (year == 0) {
            year = 1;
        }

        sTime = LocalDateTime.of(year, month, day, hour, min, sec);

        return sTime;
    }

    private DimensionType getDimType(ucar.nc2.Variable aVar) {
        String sName;
        DimensionType dimType = DimensionType.Other;
        if (_fileTypeId.equals("HDF5-EOS")) {
            sName = aVar.getShortName().toLowerCase();
            switch (sName) {
                case "longitude":
                    dimType = DimensionType.X;
                    break;
                case "latitude":
                    dimType = DimensionType.Y;
                    break;
                case "pressure":
                    dimType = DimensionType.Z;
                    break;
                case "time":
                    dimType = DimensionType.T;
                    break;
                default:
                    break;
            }
        } else {
            if (aVar.findAttributeIgnoreCase("standard_name") != null) {
                ucar.nc2.Attribute axisAtt = aVar.findAttributeIgnoreCase("standard_name");
                sName = axisAtt.getStringValue().trim().toLowerCase();
                switch (sName) {
                    case "longitude":

                    case "projection_x_coordinate":
                    case "longitude_east":
                        dimType = DimensionType.X;
                        break;
                    case "latitude":
                    case "projection_y_coordinate":
                    case "latitude_north":
                        dimType = DimensionType.Y;
                        break;
                    case "time":
                        dimType = DimensionType.T;
                        break;
                    case "level":
                        dimType = DimensionType.Z;
                        break;
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("long_name") != null) {
                    ucar.nc2.Attribute axisAtt = aVar.findAttributeIgnoreCase("long_name");
                    sName = axisAtt.getStringValue().trim().toLowerCase();
                    switch (sName) {
                        case "longitude":
                        case "coordinate longitude":
                        case "x":
                            dimType = DimensionType.X;
                            break;
                        case "latitude":
                        case "coordinate latitude":
                        case "y":
                            dimType = DimensionType.Y;
                            break;
                        case "time":
                        case "initial time":
                            dimType = DimensionType.T;
                            break;
                        case "level":
                        case "pressure":
                        case "pressure_level":
                        case "isobaric surface":
                            dimType = DimensionType.Z;
                            break;
                        default:
                            if (sName.contains("level") || sName.contains("depths")) {
                                dimType = DimensionType.Z;
                            }
                            break;
                    }
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("axis") != null) {
                    ucar.nc2.Attribute axisAtt = aVar.findAttributeIgnoreCase("axis");
                    sName = axisAtt.getStringValue().trim().toLowerCase();
                    switch (sName) {
                        case "x":
                            dimType = DimensionType.X;
                            break;
                        case "y":
                            dimType = DimensionType.Y;
                            break;
                        case "z":
                            dimType = DimensionType.Z;
                            break;
                        case "t":
                            dimType = DimensionType.T;
                            break;
                    }
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("GRIB_level_type") != null) {
                    dimType = DimensionType.Z;
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("Grib2_level_type") != null) {
                    dimType = DimensionType.Z;
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("hybrid_layer") != null) {
                    dimType = DimensionType.Z;
                }
            }
            if (dimType == DimensionType.Other) {
                if (aVar.findAttributeIgnoreCase("unitsCategory") != null) {
                    sName = aVar.findAttributeIgnoreCase("unitsCategory").getStringValue().trim().toLowerCase();
                    switch (sName) {
                        case "longitude":
                            dimType = DimensionType.X;
                            break;
                        case "latitude":
                            dimType = DimensionType.Y;
                            break;
                    }
                }
            }
            if (dimType == DimensionType.Other) {
                String vName = aVar.getShortName().toLowerCase();
                switch (vName) {
                    case "lon":
                    case "lonp":
                    case "longitude":
                    case "x":
                        dimType = DimensionType.X;
                        break;
                    case "lat":
                    case "latp":
                    case "latitude":
                    case "y":
                        dimType = DimensionType.Y;
                        break;
                    case "time":
                    case "ltime":
                        dimType = DimensionType.T;
                        break;
                    case "level":
                    case "lev":
                    case "height":
                    case "isobaric":
                    case "pressure":
                    case "depth":
                        dimType = DimensionType.Z;
                        break;
                }
            }
        }

        return dimType;
    }

    private String getGlobalAttStr(String attName) {
        String attStr = "";
        for (ucar.nc2.Attribute aAttS : this.ncAttributes) {
            if (aAttS.getShortName().equals(attName)) {
                attStr = aAttS.getValue(0).toString();
                break;
            }
        }
        return attStr;
    }

    private Conventions getConvention() {
        Conventions convention = _convention;
        boolean isIOAPI = false;
        boolean isWRFOUT = false;
        //List<String> WRFStrings = new ArrayList<String>();

        for (ucar.nc2.Attribute aAtts : ncAttributes) {
            if (aAtts.getShortName().toLowerCase().equals("ioapi_version")) {
                isIOAPI = true;
                break;
            }
            if (aAtts.getShortName().toUpperCase().equals("TITLE")) {
                String title = aAtts.getStringValue();
                if (title.toUpperCase().contains("OUTPUT FROM WRF") || title.toUpperCase().contains("OUTPUT FROM GEOGRID")
                        || title.toUpperCase().contains("OUTPUT FROM GRIDGEN") || title.toUpperCase().contains("OUTPUT FROM METGRID")) {
                    isWRFOUT = true;
                    break;
                }
                if (title.toUpperCase().contains("OUTPUT FROM") && title.toUpperCase().contains("WRF")) {
                    isWRFOUT = true;
                    break;
                }
            }
            if (aAtts.getShortName().toUpperCase().equals("WEST-EAST_GRID_DIMENSION")) {
                if (!this.getGlobalAttStr("MAP_PROJ").isEmpty()) {
                    isWRFOUT = true;
                    break;
                }
            }
        }

        if (isIOAPI) {
            convention = Conventions.IOAPI;
        }

        if (isWRFOUT) {
            convention = Conventions.WRFOUT;
        }

        return convention;
    }

    private int getDimensionIndex(ucar.nc2.Dimension dim) {
        String name2 = dim.getShortName();
        if (name2 == null) {
            return -1;
        }

        for (int i = 0; i < ncDimensions.size(); i++) {
            ucar.nc2.Dimension idim = ncDimensions.get(i);
            if (idim.getShortName().equals(name2)) {
                return i;
            }
        }

        for (int i = 0; i < ncDimensions.size(); i++) {
            ucar.nc2.Dimension idim = ncDimensions.get(i);
            if (idim.getLength() == (dim.getLength())) {
                String name1 = idim.getShortName();
                int len1 = name1.length();
                int len2 = name2.length();
                if (len1 > len2) {
                    if (name1.substring(len1 - len2).equals(name2)) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private void getProjection_CF() {
        String projStr = "";
        if (this._isHDFEOS) {
           /* ucar.nc2.Variable pVar = null;
            for (ucar.nc2.Variable aVar : ncVariables) {
                if (aVar.getShortName().equals("_HDFEOS_CRS")) {
                    pVar = aVar;
                }
            }

            if (pVar != null) {
                ucar.nc2.Attribute projAtt = pVar.findAttributeIgnoreCase("Projection");
                String proj = projAtt.getStringValue();
                if (proj.contains("GCTP_GEO")) {
                } else {
                    ucar.nc2.Attribute paraAtt = pVar.findAttributeIgnoreCase("ProjParams");
                    Array params = NCUtil.convertArray(paraAtt.getValues());
                    if (proj.contains("GCTP_SNSOID")) {
                        projStr = "+proj=sinu"
                                + "+lon_0=" + params.getObject(4).toString()
                                + "+a=" + params.getObject(0).toString()
                                + "+b=" + params.getObject(0).toString();
                    } else if (proj.contains("GCTP_CEA")) {
                        projStr = "+proj=cea"
                                + "+lon_0=" + params.getObject(4).toString()
                                + "+lat_ts=" + String.valueOf(params.getDouble(5) / 1000000);
                        //+ "+x_0=" + params.getObject(6).toString()
                        //+ "+y_0=" + params.getObject(7).toString();
                    }
                }

                ucar.nc2.Attribute ulAtt = pVar.findAttributeIgnoreCase("UpperLeftPointMtrs");
                ucar.nc2.Attribute lrAtt = pVar.findAttributeIgnoreCase("LowerRightMtrs");
                double xmin = ulAtt.getValues().getDouble(0);
                double ymax = ulAtt.getValues().getDouble(1);
                double xmax = lrAtt.getValues().getDouble(0);
                double ymin = lrAtt.getValues().getDouble(1);
                if (proj.contains("GCTP_GEO")) {
                    if (Math.abs(xmax) > 1000000) {
                        xmin = xmin / 1000000;
                        xmax = xmax / 1000000;
                        ymin = ymin / 1000000;
                        ymax = ymax / 1000000;
                    }
                }
                if (ymin > ymax) {
                    double temp = ymax;
                    ymax = ymin;
                    ymin = temp;
                    if (this._fileTypeId.equals("HDF5-EOS")) {
                        this.setYReverse(true);
                    }
                } else if (!this._fileTypeId.equals("HDF5-EOS")) {
                    this.setYReverse(true);
                }

                Dimension xDim = this.findDimension("XDim");
                Dimension yDim = this.findDimension("YDim");
                int xnum = xDim.getLength();
                int ynum = yDim.getLength();
                double xdelt = (xmax - xmin) / xnum;
                double ydelt = (ymax - ymin) / ynum;
                double[] X = new double[xnum];
                xmin = xmin + xdelt * 0.5;
                for (int i = 0; i < xnum; i++) {
                    X[i] = xmin + xdelt * i;
                }
                xDim.setDimType(DimensionType.X);
                xDim.setValues(X);
                this.setXDimension(xDim);
                double[] Y = new double[ynum];
                ymin = ymin + ydelt * 0.5;
                for (int i = 0; i < ynum; i++) {
                    Y[i] = ymin + ydelt * i;
                }
                yDim.setDimType(DimensionType.Y);
                yDim.setValues(Y);
                this.setYDimension(yDim);
            }*/
        } else {
            ucar.nc2.Variable pVar = null;
            int pvIdx = -1;
            for (ucar.nc2.Variable aVarS : ncVariables) {
                ucar.nc2.Attribute att = aVarS.findAttribute("grid_mapping_name");
                if (att != null) {
                    pVar = aVarS;
                    pvIdx = aVarS.getAttributes().indexOf(att);
                    break;
                }
            }


            if (pVar != null) {
                if (pVar.findAttribute("proj4") != null) {
                    projStr = pVar.findAttribute("proj4").getValue(0).toString();
                } else {
                    ucar.nc2.Attribute pAtt = pVar.getAttributes().get(pvIdx);
                    String attStr = pAtt.getStringValue();

                }
            }
        }


    }
}
