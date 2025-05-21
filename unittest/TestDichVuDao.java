package unittest;

import dao.DichVuDao;
import entity.DichVu;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TestDichVuDao {

    private DichVuDao dichVuDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(DichVu.class)
                .buildSessionFactory();
        dichVuDao = new DichVuDao(sessionFactory);
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    // --- Test phương thức layDanhSachDichVu ---
    // TDV_01: Kiểm tra phương thức layDanhSachDichVu, đảm bảo trả về danh sách không rỗng và không null
    @Test
    public void layDanhSachDichVu_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVu();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_02: Kiểm tra phương thức layDanhSachDichVu khi kết nối DB bị ngắt, mô phỏng lỗi
    @Test
    public void layDanhSachDichVu_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<DichVu> result = dichVuDao.layDanhSachDichVu();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // --- Test phương thức layDichVuTheoMa ---
    // TDV_03: Kiểm tra phương thức layDichVuTheoMa với mã dịch vụ hợp lệ
    @Test
    public void layDichVuTheoMa_testChuan() {
        String maDV = "DV001";
        try {
            transaction = session.beginTransaction();
            DichVu result = dichVuDao.layDichVuTheoMa(maDV);
            assertNotNull(result);
            assertEquals(maDV, result.getMaDV());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_04: Kiểm tra phương thức layDichVuTheoMa với mã dịch vụ không tồn tại
    @Test
    public void layDichVuTheoMa_testKhongTonTai() {
        String maDV = "DV999";
        try {
            transaction = session.beginTransaction();
            DichVu result = dichVuDao.layDichVuTheoMa(maDV);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức themDichVu ---
    // TDV_05: Kiểm tra phương thức themDichVu với đối tượng dịch vụ hợp lệ
    @Test
    public void themDichVu_testChuan() {
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV001");
        dichVu.setSoLuong(100);
        dichVu.setDonGia(10000.0);
        dichVu.setTrangThaiDichVu(true);
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.themDichVu(dichVu);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_06: Kiểm tra phương thức themDichVu với đối tượng null
    @Test
    public void themDichVu_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.themDichVu(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức capNhatDichVu ---
    // TDV_07: Kiểm tra phương thức capNhatDichVu với dịch vụ hợp lệ
    @Test
    public void capNhatDichVu_testChuan() {
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV001");
        dichVu.setSoLuong(50);
        dichVu.setDonGia(15000.0);
        dichVu.setTrangThaiDichVu(true);
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.capNhatDichVu(dichVu);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_08: Kiểm tra phương thức capNhatDichVu với dịch vụ không tồn tại
    @Test
    public void capNhatDichVu_testKhongTonTai() {
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV999");
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.capNhatDichVu(dichVu);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức tangSoLuongDichVu ---
    // TDV_09: Kiểm tra phương thức tangSoLuongDichVu với dịch vụ hợp lệ
    @Test
    public void tangSoLuongDihVu_testChuan() {
        String maDV = "DV001";
        int soLuong = 10;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.tangSoLuongDihVu(maDV, soLuong);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_10: Kiểm tra phương thức tangSoLuongDichVu với dịch vụ không tồn tại
    @Test
    public void tangSoLuongDihVu_testKhongTonTai() {
        String maDV = "DV999";
        int soLuong = 10;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.tangSoLuongDihVu(maDV, soLuong);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức giamSoLuongDichVu ---
    // TDV_11: Kiểm tra phương thức giamSoLuongDichVu với dịch vụ hợp lệ
    @Test
    public void giamSoLuongDichVu_testChuan() {
        String maDV = "DV001";
        int soLuong = 5;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.giamSoLuongDichVu(maDV, soLuong);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_12: Kiểm tra phương thức giamSoLuongDichVu với dịch vụ không tồn tại
    @Test
    public void giamSoLuongDichVu_testKhongTonTai() {
        String maDV = "DV999";
        int soLuong = 5;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.giamSoLuongDichVu(maDV, soLuong);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức kiemTraSoLuongDichVu ---
    // TDV_13: Kiểm tra phương thức kiemTraSoLuongDichVu với số lượng hợp lệ
    @Test
    public void kiemTraSoLuongDichVu_testChuan() {
        String maDV = "DV001";
        int soLuong = 5;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.kiemTraSoLuongDichVu(maDV, soLuong);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_14: Kiểm tra phương thức kiemTraSoLuongDichVu với dịch vụ không tồn tại
    @Test
    public void kiemTraSoLuongDichVu_testKhongTonTai() {
        String maDV = "DV999";
        int soLuong = 5;
        try {
            transaction = session.beginTransaction();
            boolean result = dichVuDao.kiemTraSoLuongDichVu(maDV, soLuong);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức layDanhSachDichVuTheoTrangThai ---
    // TDV_15: Kiểm tra phương thức layDanhSachDichVuTheoTrangThai khi trạng thái là true
    @Test
    public void layDanhSachDichVuTheoTrangThai_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTrangThai(true);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_16: Kiểm tra phương thức layDanhSachDichVuTheoTrangThai khi trạng thái là false
    @Test
    public void layDanhSachDichVuTheoTrangThai_testKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTrangThai(false);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức layDanhSachDichVuTheoNgayNhap ---
    // TDV_17: Kiểm tra phương thức layDanhSachDichVuTheoNgayNhap với ngày hợp lệ
    @Test
    public void layDanhSachDichVuTheoNgayNhap_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoNgayNhap("2021-01-06");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_18: Kiểm tra phương thức layDanhSachDichVuTheoNgayNhap với ngày không tồn tại
    @Test
    public void layDanhSachDichVuTheoNgayNhap_testKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoNgayNhap("2099-12-31");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức layDanhSachDichVuTheoNgayTen ---
    // TDV_19: Kiểm tra phương thức layDanhSachDichVuTheoNgayTen với tên dịch vụ hợp lệ
    @Test
    public void layDanhSachDichVuTheoNgayTen_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoNgayTen("Bia");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // TDV_20: Kiểm tra phương thức layDanhSachDichVuTheoNgayTen với tên dịch vụ không tồn tại
    @Test
    public void layDanhSachDichVuTheoNgayTen_testKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoNgayTen("KhongTonTai");
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // --- Test phương thức layDanhSachDichVuTheoTenNgayTrangThai ---
    // TDV_21: Kiểm tra phương thức layDanhSachDichVuTheoTenNgayTrangThai với tham số hợp lệ
    @Test
    public void layDanhSachDichVuTheoTenNgayTrangThai_testChuan() {
        try {
            transaction = session.beginTransaction();
            int page = 0;
            int limit = 10;
            String ten = "Bia";
            int selected = 1; // Trạng thái active
            
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTenNgayTrangThai(page, limit, ten, selected);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_22: Kiểm tra phương thức layDanhSachDichVuTheoTenNgayTrangThai với tên dịch vụ không tồn tại
    @Test
    public void layDanhSachDichVuTheoTenNgayTrangThai_testTenKhongTonTai() {
        try {
            transaction = session.beginTransaction();
            int page = 0;
            int limit = 10;
            String ten = "KhongTonTai";
            int selected = 1; // Trạng thái active
            
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTenNgayTrangThai(page, limit, ten, selected);
            assertNotNull(result);
            assertTrue(result.isEmpty()); // Danh sách rỗng vì không có dịch vụ nào tên "KhongTonTai"
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_23: Kiểm tra phương thức layDanhSachDichVuTheoTenNgayTrangThai với trạng thái inactive
    @Test
    public void layDanhSachDichVuTheoTenNgayTrangThai_testInactive() {
        try {
            transaction = session.beginTransaction();
            int page = 0;
            int limit = 10;
            String ten = "";
            int selected = 2; // Trạng thái inactive
            
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTenNgayTrangThai(page, limit, ten, selected);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_24: Kiểm tra phương thức layDanhSachDichVuTheoTenNgayTrangThai với tham số page và limit không hợp lệ
    @Test
    public void layDanhSachDichVuTheoTenNgayTrangThai_testPaginationInvalid() {
        try {
            transaction = session.beginTransaction();
            int page = -1; // Invalid page
            int limit = 0; // Invalid limit
            String ten = "";
            int selected = 0; // All statuses
            
            List<DichVu> result = dichVuDao.layDanhSachDichVuTheoTenNgayTrangThai(page, limit, ten, selected);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // --- Test phương thức tongTrang ---
    // TDV_25: Kiểm tra phương thức tongTrang với tham số hợp lệ
    @Test
    public void tongTrang_testChuan() {
        try {
            transaction = session.beginTransaction();
            String txtSearch = "";
            int trangThai = 1; // Active
            int limit = 10;
            
            int result = dichVuDao.tongTrang(txtSearch, trangThai, limit);
            assertTrue(result >= 0); // Số trang phải là số không âm
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_26: Kiểm tra phương thức tongTrang với tham số tìm kiếm không tồn tại
    @Test
    public void tongTrang_testKhongCoKetQua() {
        try {
            transaction = session.beginTransaction();
            String txtSearch = "KhongTonTai";
            int trangThai = 1; // Active
            int limit = 10;
            
            int result = dichVuDao.tongTrang(txtSearch, trangThai, limit);
            assertEquals(0, result); // Không có kết quả nên số trang là 0
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_27: Kiểm tra phương thức tongTrang với limit không hợp lệ
    @Test
    public void tongTrang_testLimitInvalid() {
        try {
            transaction = session.beginTransaction();
            String txtSearch = "";
            int trangThai = 1; // Active
            int limit = -5; // Invalid limit
            
            int result = dichVuDao.tongTrang(txtSearch, trangThai, limit);
            assertTrue(result >= 0); // Hàm nên xử lý giá trị limit không hợp lệ và vẫn trả về số trang hợp lệ
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_28: Kiểm tra phương thức tongTrang với trạng thái không hợp lệ
    @Test
    public void tongTrang_testTrangThaiInvalid() {
        try {
            transaction = session.beginTransaction();
            String txtSearch = "";
            int trangThai = 3; // Invalid status code
            int limit = 10;
            
            int result = dichVuDao.tongTrang(txtSearch, trangThai, limit);
            assertTrue(result >= 0); // Hàm nên xử lý giá trị trạng thái không hợp lệ
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // --- Test giamSoLuongDichVu edge cases ---
    // TDV_29: Kiểm tra phương thức giamSoLuongDichVu khi số lượng giảm lớn hơn số lượng hiện có
    @Test
    public void giamSoLuongDichVu_testSoLuongVuotQua() {
        String maDV = "DV001";
        int soLuongHienTai = 0;
        
        try {
            // Lấy số lượng hiện tại
            transaction = session.beginTransaction();
            DichVu dv = dichVuDao.layDichVuTheoMa(maDV);
            if (dv != null) {
                soLuongHienTai = dv.getSoLuong();
            }
            transaction.commit();
            
            // Thử giảm nhiều hơn số lượng hiện có
            transaction = session.beginTransaction();
            boolean result = dichVuDao.giamSoLuongDichVu(maDV, soLuongHienTai + 1);
            assertFalse(result); // Không thể giảm nhiều hơn số lượng hiện có
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    // TDV_30: Kiểm tra phương thức kiemTraSoLuongDichVu khi số lượng cần kiểm tra lớn hơn số lượng hiện có
    @Test
    public void kiemTraSoLuongDichVu_testSoLuongVuotQua() {
        String maDV = "DV001";
        int soLuongHienTai = 0;
        
        try {
            // Lấy số lượng hiện tại
            transaction = session.beginTransaction();
            DichVu dv = dichVuDao.layDichVuTheoMa(maDV);
            if (dv != null) {
                soLuongHienTai = dv.getSoLuong();
            }
            transaction.commit();
            
            // Kiểm tra số lượng lớn hơn hiện có
            transaction = session.beginTransaction();
            boolean result = dichVuDao.kiemTraSoLuongDichVu(maDV, soLuongHienTai + 1);
            assertFalse(result); // Không đủ số lượng
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
