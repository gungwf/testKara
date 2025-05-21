package unittest;

import view.*;
import org.junit.Test;
import static org.junit.Assert.*;
import entity.NhanVien;

public class TestViewConstructors {
    
    @Test
    public void testDangNhap() {
        try {
            DangNhap view = new DangNhap();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo DangNhap: " + e.getMessage());
        }
    }

    @Test
    public void testGiaoDienChinh() {
        try {
            GiaoDienChinh view = new GiaoDienChinh();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo GiaoDienChinh: " + e.getMessage());
        }
    }

    @Test
    public void testMainFrame() {
        try {
            NhanVien nhanVien = new NhanVien("NV001");
            MainFrame view = new MainFrame(nhanVien);
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo MainFrame: " + e.getMessage());
        }
    }

    @Test
    public void testDanhSachPhongPanel() {
        try {
            DanhSachPhongPanel view = new DanhSachPhongPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo DanhSachPhongPanel: " + e.getMessage());
        }
    }

    @Test
    public void testKhachHangPanel() {
        try {
            KhachHangPanel view = new KhachHangPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo KhachHangPanel: " + e.getMessage());
        }
    }

    @Test
    public void testNhanVienPanel() {
        try {
            NhanVienPanel view = new NhanVienPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo NhanVienPanel: " + e.getMessage());
        }
    }

    @Test
    public void testDSDichVuPanel() {
        try {
            DSDichVuPanel view = new DSDichVuPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo DSDichVuPanel: " + e.getMessage());
        }
    }

    @Test
    public void testDSKhuyenMaiPanel() {
        try {
            DSKhuyenMaiPanel view = new DSKhuyenMaiPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo DSKhuyenMaiPanel: " + e.getMessage());
        }
    }

    @Test
    public void testHoaDonPanel() {
        try {
            HoaDonPanel view = new HoaDonPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo HoaDonPanel: " + e.getMessage());
        }
    }

    @Test
    public void testThongKePanel() {
        try {
            ThongKePanel view = new ThongKePanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo ThongKePanel: " + e.getMessage());
        }
    }

    @Test
    public void testThongKeTheoNgayPanel() {
        try {
            ThongKeTheoNgayPanel view = new ThongKeTheoNgayPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo ThongKeTheoNgayPanel: " + e.getMessage());
        }
    }

//    @Test
//    public void testThongKeTheoThangPanel() {
//        try {
//            ThongKeTheoThangPanel view = new ThongKeTheoThangPanel();
//            assertNotNull(view);
//        } catch (Exception e) {
//            fail("Không thể khởi tạo ThongKeTheoThangPanel: " + e.getMessage());
//        }
//    }

//    @Test
//    public void testThongKeTheoNamPanel() {
//        try {
//            ThongKeTheoNamPanel view = new ThongKeTheoNamPanel();
//            assertNotNull(view);
//        } catch (Exception e) {
//            fail("Không thể khởi tạo ThongKeTheoNamPanel: " + e.getMessage());
//        }
//    }

    @Test
    public void testThongKeKhachHangPanel() {
        try {
            ThongKeKhachHangPanel view = new ThongKeKhachHangPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo ThongKeKhachHangPanel: " + e.getMessage());
        }
    }

    @Test
    public void testChiTietPhongPanel() {
        try {
            ChiTietPhongPanel view = new ChiTietPhongPanel();
            assertNotNull(view);
        } catch (Exception e) {
            fail("Không thể khởi tạo ChiTietPhongPanel: " + e.getMessage());
        }
    }
}
