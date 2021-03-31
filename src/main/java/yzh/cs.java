package yzh;

import org.junit.Assert;
import org.junit.Test;
import ucar.nc2.grib.grib1.Grib1Gds;
import ucar.nc2.grib.grib1.Grib1Record;
import ucar.nc2.grib.grib1.Grib1RecordScanner;
import ucar.nc2.grib.grib1.Grib1SectionProductDefinition;
import ucar.nc2.grib.grib1.tables.Grib1Customizer;
import ucar.nc2.grib.grib1.tables.Grib1ParamTableReader;
import ucar.nc2.time.CalendarDate;
import ucar.unidata.io.RandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;

public class cs {
    String filename = "C:\\Users\\杨泽华\\Desktop\\文件临时\\1\\W_NAFP_C_ECMF_20200830184515_P_C3E08301200083018001-ACHN.GRB";
    ;
    boolean check;
    int gdsTemplate;
    int param;
    long datalen;
    CalendarDate refdate;

    interface Callback {
        boolean call(RandomAccessFile raf, Grib1Record gr) throws IOException;
    }

    @Test
    public void mycs() {
        String dirS = "C:\\Users\\杨泽华\\Desktop\\文件临时\\1\\W_NAFP_C_ECMF_20200830184515_P_C3E08301200083018001-ACHN.GRB";

    }

    @Test
    public void testRead() throws IOException {
        readFile(filename, (raf, gr) -> {
            Grib1Gds gds = gr.getGDS();
            if (check) {
                Assert.assertEquals(gdsTemplate, gds.template);
            }
            if (check) {
                Assert.assertTrue(gds.toString().contains("template=" + gdsTemplate));
            }
            gds.testHorizCoordSys(new Formatter());

            Grib1SectionProductDefinition pds = gr.getPDSsection();
            if (check) {
                Assert.assertEquals(param, pds.getParameterNumber());
            }
            Formatter f = new Formatter();
            pds.showPds(Grib1Customizer.factory(gr, null), f);
            if (check) {
                Assert.assertTrue(f.toString().contains(String.format("Parameter Name : (%d)", param))
                        || f.toString().contains(String.format("Parameter %d not found", param)));
            }

            if (check) {
                Assert.assertEquals(this.refdate, pds.getReferenceDate());
            }

            float[] data = gr.readData(raf);
            if (check) {
                Assert.assertEquals(datalen, data.length);
            }
            System.out.printf("%s: template,param,len=  %d, %d, %d, %s %n", filename, gds.template, pds.getParameterNumber(),
                    data.length, pds.getReferenceDate());
            return true;
        });
    }

    private void readFile(String path, Callback callback) throws IOException {
        try (RandomAccessFile raf = new ucar.unidata.io.RandomAccessFile(path, "r")) {
            raf.order(ucar.unidata.io.RandomAccessFile.BIG_ENDIAN);
            raf.seek(0);

            Grib1RecordScanner reader = new Grib1RecordScanner(raf);
            while (reader.hasNext()) {
                ucar.nc2.grib.grib1.Grib1Record gr = reader.next();
                if (gr == null)
                    break;
                callback.call(raf, gr);
            }

        }
    }

}
