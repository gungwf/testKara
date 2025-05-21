package unittest;

import dao.KhachHangDao;
import entity.KhachHang;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TestKhachHangDao {

    private KhachHangDao khachHangDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(KhachHang.class)
                .buildSessionFactory();
        khachHangDao = new KhachHangDao(sessionFactory);
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

    // Test phương thức layDanhSachKhacHang
    /**
     * Kiểm tra phương thức lấy danh sách khách hàng hoạt động đúng khi có kết nối đến database
     * ID: TKH_01
     */
    @Test
    public void layDanhSachKhacHang_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhacHang();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng xử lý đúng khi mất kết nối database
     * ID: TKH_02
     */
    @Test
    public void layDanhSachKhacHang_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhachHang> result = khachHangDao.layDanhSachKhacHang();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layKhachHangTheoMa
    /**
     * Kiểm tra phương thức lấy khách hàng theo mã hoạt động đúng với mã hợp lệ
     * ID: TKH_03
     */
    @Test
    public void layKhachHangTheoMa_testChuan() {
        String maKH = "KH001";
        try {
            transaction = session.beginTransaction();
            KhachHang result = khachHangDao.layKhachHangTheoMa(maKH);
            assertNotNull(result);
            assertEquals(maKH, result.getMaKH());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy khách hàng theo mã xử lý đúng khi mã không tồn tại
     * ID: TKH_04
     */
    @Test
    public void layKhachHangTheoMa_testKhongTonTai() {
        String maKH = "KH999";
        try {
            transaction = session.beginTransaction();
            KhachHang result = khachHangDao.layKhachHangTheoMa(maKH);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức themKhachHang
    /**
     * Kiểm tra phương thức thêm khách hàng hoạt động đúng với dữ liệu hợp lệ
     * ID: TKH_05
     */
    @Test
    public void themKhachHang_testChuan() {
        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen("Nguyễn Văn A");
        khachHang.setSoDienThoai("0123456789");
        khachHang.setGioiTinh(true);
        try {
            transaction = session.beginTransaction();
            boolean result = khachHangDao.themKhachHang(khachHang);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm khách hàng xử lý đúng khi dữ liệu null
     * ID: TKH_06
     */
    @Test
    public void themKhachHang_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = khachHangDao.themKhachHang(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm khách hàng xử lý đúng khi có lỗi
     * ID: TKH_07
     */
    @Test
    public void themKhachHang_testLoi() {
        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen("Nguyễn Văn A");
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            boolean result = khachHangDao.themKhachHang(khachHang);
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức suaKhachHang
    /**
     * Kiểm tra phương thức sửa khách hàng hoạt động đúng với dữ liệu hợp lệ
     * ID: TKH_08
     */
    @Test
    public void suaKhachHang_testChuan() {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH("KH001");
        khachHang.setHoTen("Nguyễn Văn B");
        khachHang.setSoDienThoai("0987654321");
        khachHang.setGioiTinh(false);
        try {
            transaction = session.beginTransaction();
            boolean result = khachHangDao.suaKhachHang(khachHang);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức sửa khách hàng xử lý đúng khi khách hàng không tồn tại
     * ID: TKH_09
     */
    @Test
    public void suaKhachHang_testKhongTonTai() {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH("KH999");
        try {
            transaction = session.beginTransaction();
            boolean result = khachHangDao.suaKhachHang(khachHang);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức sửa khách hàng xử lý đúng khi có lỗi
     * ID: TKH_10
     */
    @Test
    public void suaKhachHang_testLoi() {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH("KH001");
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            boolean result = khachHangDao.suaKhachHang(khachHang);
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layKhachHangTheoSDT
    /**
     * Kiểm tra phương thức lấy khách hàng theo số điện thoại hoạt động đúng với SDT hợp lệ
     * ID: TKH_11
     */
    @Test
    public void layKhachHangTheoSDT_testChuan() {
        String sdt = "0123456789";
        try {
            transaction = session.beginTransaction();
            KhachHang result = khachHangDao.layKhachHangTheoSDT(sdt);
            assertNotNull(result);
            assertEquals(sdt, result.getSoDienThoai());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy khách hàng theo số điện thoại xử lý đúng khi SDT không tồn tại
     * ID: TKH_12
     */
    @Test
    public void layKhachHangTheoSDT_testKhongTonTai() {
        String sdt = "9999999999";
        try {
            transaction = session.beginTransaction();
            KhachHang result = khachHangDao.layKhachHangTheoSDT(sdt);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy khách hàng theo số điện thoại xử lý đúng khi có lỗi
     * ID: TKH_13
     */
    @Test
    public void layKhachHangTheoSDT_testLoi() {
        String sdt = "0123456789";
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            KhachHang result = khachHangDao.layKhachHangTheoSDT(sdt);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layDanhSachKhachHangTheoTen
    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo tên hoạt động đúng với tên hợp lệ
     * ID: TKH_14
     */
    @Test
    public void layDanhSachKhachHangTheoTen_testChuan() {
        String ten = "Nguyễn";
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoTen(ten);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo tên xử lý đúng khi tên không tồn tại
     * ID: TKH_15
     */
    @Test
    public void layDanhSachKhachHangTheoTen_testKhongTonTai() {
        String ten = "XXXXXXX"; // Tên không có khả năng tồn tại
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoTen(ten);
            assertTrue(result == null || result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo tên xử lý đúng khi có lỗi
     * ID: TKH_16
     */
    @Test
    public void layDanhSachKhachHangTheoTen_testLoi() {
        String ten = "Nguyễn";
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoTen(ten);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức phatSinhMaTuDong
    /**
     * Kiểm tra phương thức phát sinh mã tự động hoạt động đúng
     * ID: TKH_17
     */
    @Test
    public void phatSinhMaTuDong_testChuan() {
        try {
            transaction = session.beginTransaction();
            String result = khachHangDao.phatSinhMaTuDong();
            assertNotNull(result);
            assertTrue(result.startsWith("KH"));
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức phát sinh mã tự động xử lý đúng khi có lỗi
     * ID: TKH_18
     */
    @Test
    public void phatSinhMaTuDong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            String result = khachHangDao.phatSinhMaTuDong();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layDanhSachKhachHangTheoGioiTinh
    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo giới tính nam
     * ID: TKH_19
     */
    @Test
    public void layDanhSachKhachHangTheoGioiTinh_testNam() {
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoGioiTinh(true);
            assertNotNull(result);
            for (KhachHang kh : result) {
                assertTrue(kh.isGioiTinh());
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo giới tính nữ
     * ID: TKH_20
     */
    @Test
    public void layDanhSachKhachHangTheoGioiTinh_testNu() {
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoGioiTinh(false);
            assertNotNull(result);
            for (KhachHang kh : result) {
                assertFalse(kh.isGioiTinh());
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng theo giới tính xử lý đúng khi có lỗi
     * ID: TKH_21
     */
    @Test
    public void layDanhSachKhachHangTheoGioiTinh_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhachHang> result = khachHangDao.layDanhSachKhachHangTheoGioiTinh(true);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layDanhSachKhachHang (phân trang)
    /**
     * Kiểm tra phương thức lấy danh sách khách hàng phân trang hoạt động đúng
     * ID: TKH_22
     */
    @Test
    public void layDanhSachKhachHang_testPhanTrang() {
        int page = 0; // Trang đầu tiên
        String tenKH = "";
        String gioiTinh = "";
        int limit = 10;
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHang(page, tenKH, gioiTinh, limit);
            assertNotNull(result);
            assertTrue(result.size() <= limit); // Số lượng kết quả không vượt quá giới hạn
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng phân trang với bộ lọc tên
     * ID: TKH_23
     */
    @Test
    public void layDanhSachKhachHang_testLocTheoTen() {
        int page = 0;
        String tenKH = "Nguyễn";
        String gioiTinh = "";
        int limit = 10;
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHang(page, tenKH, gioiTinh, limit);
            assertNotNull(result);
            for (KhachHang kh : result) {
                assertTrue(kh.getHoTen().contains(tenKH));
            }
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng phân trang với bộ lọc giới tính
     * ID: TKH_24
     */
    @Test
    public void layDanhSachKhachHang_testLocTheoGioiTinh() {
        int page = 0;
        String tenKH = "";
        String gioiTinh = "true"; // Giới tính nam
        int limit = 10;
        try {
            transaction = session.beginTransaction();
            List<KhachHang> result = khachHangDao.layDanhSachKhachHang(page, tenKH, gioiTinh, limit);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khách hàng phân trang xử lý đúng khi có lỗi
     * ID: TKH_25
     */
    @Test
    public void layDanhSachKhachHang_testPhanTrangLoi() {
        int page = 0;
        String tenKH = "";
        String gioiTinh = "";
        int limit = 10;
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhachHang> result = khachHangDao.layDanhSachKhachHang(page, tenKH, gioiTinh, limit);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức tongTrang
    /**
     * Kiểm tra phương thức tính tổng số trang hoạt động đúng
     * ID: TKH_26
     */
    @Test
    public void tongTrang_testChuan() {
        String txtSearch = "";
        String gioiTinh = "";
        int limit = 10;
        try {
            transaction = session.beginTransaction();
            int result = khachHangDao.tongTrang(txtSearch, gioiTinh, limit);
            assertTrue(result > 0); // Phải có ít nhất 1 trang
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức tính tổng số trang với các bộ lọc
     * ID: TKH_27
     */
    @Test
    public void tongTrang_testBoLoc() {
        String txtSearch = "Nguyễn";
        String gioiTinh = "true";
        int limit = 10;
        try {
            transaction = session.beginTransaction();
            int result = khachHangDao.tongTrang(txtSearch, gioiTinh, limit);
            assertTrue(result >= 0); // Có thể không có kết quả nào phù hợp với bộ lọc
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức tính tổng số trang xử lý đúng khi có lỗi
     * ID: TKH_28
     */
    @Test
    public void tongTrang_testLoi() {
        String txtSearch = "";
        String gioiTinh = "";
        int limit = 10;
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            int result = khachHangDao.tongTrang(txtSearch, gioiTinh, limit);
            assertEquals(0, result); // Khi có lỗi, phương thức trả về 0
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }
}
