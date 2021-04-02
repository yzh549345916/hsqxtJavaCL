package yzh.数值预报处理;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.*;
import ucar.nc2.util.CompareNetcdf2;
import ucar.nc2.write.NetcdfCopier;
import ucar.nc2.write.NetcdfFormatWriter;

import java.io.IOException;


public class nc处理 {

    /**
     * 从全球nc数据中裁剪指定区域的数据
     *
     * @param resourPath nc文件所在位置路径
     * @param startLon   指定区域开始的经度
     * @param startLat   指定区域开始的纬度
     * @param latCount   纬度要读取多少个点
     * @param lonCount   经度要读取多少个点
     * @param begin      从时间纬度的第几层开始读取
     * @param end        第几层结束
     * @return 指定区域的数据
     */
    public static short[][] readNCfile(String resourPath, int latCount, int lonCount, float startLon, float startLat, int begin,
                                       int end) {

        try {

            NetcdfFile ncFile = NetcdfFiles.open(resourPath); //获取源文件
            Variable v = ncFile.findVariable("qpf_ml"); //读取qpf_ml的变量，
            //
            short[][] values = null;
            for (int i = begin; i < end; i++) {    //本读取的qpf_ml是一个3维数据，时间、经度、维度，一下子把3维数据全部读出来会很大，时间维度是24层，所以通过遍历时间维度获取数据，i为时间维度的层数

                int[] origin = {i, (int) ((111 - startLat) * 100), (int) ((startLon - 111) * 100)};//origin 设置维度准备从哪个位置开始读取数据
                int[] shape = {1, latCount, lonCount};//shape 和origin对应，设置读取多少点后结束
                short[][] temp = (short[][]) v.read(origin, shape).reduce(0).copyToNDJavaArray(); //去掉时间维度，变为二维

                if (values != null) {
                    for (int j = 0; j < latCount; j++) {
                        for (int k = 0; k < lonCount; k++) {
                            values[j][k] += temp[j][k];
                        }
                    }
                } else {
                    values = temp;
                }

            }
            ncFile.close();
            return values;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static short[][] readNCfile(String resourPath) {

        try {
            NetcdfFile ncFile = NetcdfFiles.open(resourPath); //获取源文件
            String _fileTypeStr = ncFile.getFileTypeDescription();
            String _fileTypeId = ncFile.getFileTypeId();
            NetcdfFile.Builder buil = ncFile.toBuilder();

            剔除nc要素("D:\\cx\\java\\呼市气象台java处理\\target\\区台数值预报文件\\szyb\\huanbao\\CUACE\\2021-03-30\\cs.nc", buil);

            var featureType = ncFile.getGlobalAttributes();
            var sss = NetcdfFormatWriter.createNewNetcdf3("D:\\cx\\java\\呼市气象台java处理\\target\\区台数值预报文件\\szyb\\huanbao\\CUACE\\2021-03-30\\cs.nc");
            ImmutableList<Variable> variables = ncFile.getVariables();

            for (Variable variable : variables
            ) {
                int rank = variable.getRank();

                var aa = variable.attributes();
                if (rank > 2) {
                    for (int i = 0; i < rank; i++) {
                        Dimension dim = variable.getDimension(i);

                        dim.getName();
                    }

                } else if (rank == 1) {
                    var s1 = variable.read();
                    s1.getRank();
                }
                String ss2 = "";
            }
            short[][] values = null;

            ncFile.close();
            return values;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean compressCUACE(String sPath, String dPath) {

        try {

            NetcdfFile ncFile = NetcdfFiles.open(sPath);
            NetcdfFormatWriter.Builder builder = NetcdfFormatWriter.createNewNetcdf3(dPath);
            var ncb = ncFile.toBuilder();
            ncb.rootGroup.removeVariable("V");
            ncb.rootGroup.removeVariable("U");
            ncb.rootGroup.removeVariable("T");
            ncb.rootGroup.removeVariable("T2");
            NetcdfCopier copier = NetcdfCopier.create(ncb.build(), builder);
            copier.write(null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    public void cs() {
        System.out.println(compressCUACE("E:\\1.nc","E:\\123.nc"));
    }

    private static boolean 剔除nc要素(String path, NetcdfFile.Builder builder1) {
        boolean bs = false;
        NetcdfFormatWriter.Builder builder = NetcdfFormatWriter.createNewNetcdf3(path);
        var mync = builder1.build();
        for (var dim : builder1.rootGroup.getDimensions()
        ) {
            builder.addDimension(dim);

        }

        for (var item : mync.getVariables()
        ) {
            builder.addVariable(item.getShortName(), item.getDataType(), item.getDimensions());
        }
        try {
            builder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bs;
    }

}
