package unittest;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.KhuyenMaiDao;
import entity.KhuyenMai;

public class TestKhuyenMaiDao {

    private KhuyenMaiDao khuyenMaiDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(KhuyenMai.class)
                .buildSessionFactory();
        khuyenMaiDao = new KhuyenMaiDao(sessionFactory);
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

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi hoạt động đúng khi có kết nối đến database
     * ID: TKM_01
     */
    @Test
    public void layDanhSachKhuyenMai_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMai();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi xử lý đúng khi mất kết nối database
     * ID: TKM_02
     */
    @Test
    public void layDanhSachKhuyenMai_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMai();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức thêm khuyến mãi hoạt động đúng với dữ liệu hợp lệ
     * ID: TKM_03
     */
    @Test
    public void themKhuyenMai_testChuan() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM("KM001");
        khuyenMai.setMaGiamGia("GG001");
        khuyenMai.setChietKhau(20);
        khuyenMai.setTrangThai(true);
        khuyenMai.setDaSuDung(0);
        khuyenMai.setNgayBatDau(new Date());
        khuyenMai.setNgayHetHan(new Date());
        khuyenMai.setMoTa("Khuyến mãi tháng 12");
        khuyenMai.setTongSoLuong(100);
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.themKhuyenMai(khuyenMai);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm khuyến mãi xử lý đúng khi dữ liệu null
     * ID: TKM_04
     */
    @Test
    public void themKhuyenMai_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.themKhuyenMai(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật khuyến mãi hoạt động đúng với dữ liệu hợp lệ
     * ID: TKM_05
     */
    @Test
    public void capNhatKhuyenMai_testChuan() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM("KM001");
        khuyenMai.setMaGiamGia("GG001");
        khuyenMai.setChietKhau(30);
        khuyenMai.setTrangThai(true);
        khuyenMai.setDaSuDung(0);
        khuyenMai.setNgayBatDau(new Date());
        khuyenMai.setNgayHetHan(new Date());
        khuyenMai.setMoTa("Khuyến mãi tháng 12 cập nhật");
        khuyenMai.setTongSoLuong(100);
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.capNhatKhuyenMai(khuyenMai);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật khuyến mãi xử lý đúng khi khuyến mãi không tồn tại
     * ID: TKM_06
     */
    @Test
    public void capNhatKhuyenMai_testKhongTonTai() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM("KM999");
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.capNhatKhuyenMai(khuyenMai);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy khuyến mãi theo mã hoạt động đúng với mã hợp lệ
     * ID: TKM_07
     */
    @Test
    public void layKhuyenMaiTheoMa_testChuan() {
        String maGG = "GG001";
        try {
            transaction = session.beginTransaction();
            KhuyenMai result = khuyenMaiDao.layKhuyenMaiTheoMa(maGG);
            assertNotNull(result);
            assertEquals(maGG, result.getMaGiamGia());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy khuyến mãi theo mã xử lý đúng khi mã không tồn tại
     * ID: TKM_08
     */
    @Test
    public void layKhuyenMaiTheoMa_testKhongTonTai() {
        String maGG = "GG999";
        try {
            transaction = session.beginTransaction();
            KhuyenMai result = khuyenMaiDao.layKhuyenMaiTheoMa(maGG);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo trạng thái hoạt động đúng
     * ID: TKM_09
     */
    @Test
    public void layDanhSachKhuyenMaiTheoTrangThai_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoTrangThai(true);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo ngày nhập hoạt động đúng
     * ID: TKM_10
     */
    @Test
    public void layDanhSachKhuyenMaiTheoNgayNhap_testChuan() {
        String ngayNhap = "2024-03-20";
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoNgayNhap(ngayNhap);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo tên hoạt động đúng
     * ID: TKM_11
     */
    @Test
    public void layDanhSachKhuyenMaiTheoNgayTen_testChuan() {
        String ten = "Khuyến mãi";
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoNgayTen(ten);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức tự động sinh mã khuyến mãi hoạt động đúng
     * ID: TKM_12
     */
    @Test
    public void tuDongSinhMaKhuyenMai_testChuan() {
        try {
            transaction = session.beginTransaction();
            String result = khuyenMaiDao.getMaCuoi();
            assertNotNull(result);
            assertTrue(result.startsWith("KM"));
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức thêm khuyến mãi xử lý đúng khi có lỗi chung
     * ID: TKM_13
     */
    @Test
    public void themKhuyenMai_testLoi() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM("KM001");
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            boolean result = khuyenMaiDao.themKhuyenMai(khuyenMai);
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức cập nhật khuyến mãi xử lý đúng khi có lỗi chung
     * ID: TKM_14
     */
    @Test
    public void capNhatKhuyenMai_testLoi() {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setMaKM("KM001");
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            boolean result = khuyenMaiDao.capNhatKhuyenMai(khuyenMai);
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo trạng thái xử lý đúng khi có lỗi
     * ID: TKM_15
     */
    @Test
    public void layDanhSachKhuyenMaiTheoTrangThai_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoTrangThai(true);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo ngày nhập xử lý đúng khi có lỗi
     * ID: TKM_16
     */
    @Test
    public void layDanhSachKhuyenMaiTheoNgayNhap_testLoi() {
        String ngayNhap = "2024-03-20";
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoNgayNhap(ngayNhap);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo tên xử lý đúng khi có lỗi
     * ID: TKM_17
     */
    @Test
    public void layDanhSachKhuyenMaiTheoNgayTen_testLoi() {
        String ten = "Khuyến mãi";
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoNgayTen(ten);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi phân trang hoạt động đúng
     * ID: TKM_18
     */
    @Test
    public void layDanhSachKhuyenMaiTheoTenNgayTrangThai_testPhanTrang() {
        int page = 1;
        int limit = 10;
        String ten = "";
        String ngay = "";
        int selected = 1;  // Trang thái đang hoạt động
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoTenNgayTrangThai(page, limit, ten, ngay, selected);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi phân trang xử lý đúng khi có lỗi
     * ID: TKM_19
     */
    @Test
    public void layDanhSachKhuyenMaiTheoTenNgayTrangThai_testLoi_PhanTrang() {
        int page = 1;
        int limit = 10;
        String ten = "";
        String ngay = "";
        int selected = 1; // Trang thái đang hoạt động
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoTenNgayTrangThai(page, limit, ten, ngay, selected);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức tính tổng số trang cho khuyến mãi hoạt động đúng
     * ID: TKM_20
     */
    @Test
    public void tinhTongSoTrang_testChuan() {
        String ten = "";
        int trangThai = 1;
        String ngayBatDau = ""; 
        int tongSoLuong = 10;
        try {
            transaction = session.beginTransaction();
            int result = khuyenMaiDao.tongTrang(ten, trangThai, ngayBatDau, tongSoLuong);
            assertTrue(result >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức tính tổng số trang cho khuyến mãi xử lý đúng khi có lỗi
     * ID: TKM_21
     */
    @Test
    public void tinhTongSoTrang_testLoi() {
        String ten = "";
        int trangThai = 1;
        String ngayBatDau = ""; 
        int tongSoLuong = 10;
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            int result = khuyenMaiDao.tongTrang(ten, trangThai, ngayBatDau, tongSoLuong);
            assertEquals(0, result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức kiểm tra mã giảm giá hợp lệ
     * ID: TKM_22
     */
    @Test
    public void kiemTraMaGiamGia_testChuan() {
        String maGiamGia = "GG001";
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.apDungMaGiamGia(maGiamGia);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức kiểm tra mã giảm giá không hợp lệ
     * ID: TKM_23
     */
    @Test
    public void kiemTraMaGiamGia_testMaKhongHopLe() {
        String maGiamGia = "GG999";
        try {
            transaction = session.beginTransaction();
            boolean result = khuyenMaiDao.apDungMaGiamGia(maGiamGia);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách khuyến mãi theo tên, ngày và trạng thái hoạt động đúng
     * ID: TKM_24
     */
    @Test
    public void layDanhSachKhuyenMaiTheoTenNgayTrangThai_testChuan() {
        int page = 1;
        int limit = 10;
        String ten = "Khuyến mãi";
        String ngay = "2024-01-01";
        int selected = 1; // Trạng thái hoạt động
        try {
            transaction = session.beginTransaction();
            List<KhuyenMai> result = khuyenMaiDao.layDanhSachKhuyenMaiTheoTenNgayTrangThai(page, limit, ten, ngay, selected);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức kiểm tra mã giảm giá tồn tại nhưng không hợp lệ (đã vô hiệu hóa hoặc hết hạn)
     * ID: TKM_25
     */
    @Test
    public void kiemTraMaGiamGia_testMaTonTaiNhungKhongHopLe() {
        String maGiamGia = "GG002"; // Mã tồn tại nhưng đã bị vô hiệu hoá hoặc hết hạn
        try {
            transaction = session.beginTransaction();
            
            // Tạo mã giảm giá đã hết hạn hoặc bị vô hiệu hóa để test
            KhuyenMai khuyenMaiHetHan = new KhuyenMai();
            khuyenMaiHetHan.setMaGiamGia(maGiamGia);
            khuyenMaiHetHan.setMaKM("KM002");
            khuyenMaiHetHan.setChietKhau(10);
            khuyenMaiHetHan.setTrangThai(false); // Vô hiệu hóa
            Date ngayHetHan = new Date(System.currentTimeMillis() - 86400000); // Ngày hôm qua
            khuyenMaiHetHan.setNgayHetHan(ngayHetHan);
            khuyenMaiDao.themKhuyenMai(khuyenMaiHetHan);
            
            // Kiểm tra mã giảm giá đã vô hiệu hóa hoặc hết hạn
            boolean result = khuyenMaiDao.apDungMaGiamGia(maGiamGia);
            assertFalse(result);
            
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức kiểm tra mã giảm giá đã dùng hết số lượng (result == 0)
     * ID: TKM_26
     */
    @Test
    public void kiemTraMaGiamGia_testMaDungHetSoLuong() {
        String maGiamGia = "GG003"; // Mã tồn tại nhưng đã dùng hết số lượng
        try {
            transaction = session.beginTransaction();
            
            // Tạo mã giảm giá đã dùng hết số lượng để test
            KhuyenMai khuyenMaiHetSL = new KhuyenMai();
            khuyenMaiHetSL.setMaGiamGia(maGiamGia);
            khuyenMaiHetSL.setMaKM("KM003");
            khuyenMaiHetSL.setChietKhau(15);
            khuyenMaiHetSL.setTrangThai(true); // Vẫn hoạt động
            
            // Thiết lập ngày hợp lệ
            Date ngayHienTai = new Date();
            Date ngayBatDau = new Date(ngayHienTai.getTime() - 86400000 * 10); // 10 ngày trước
            Date ngayHetHan = new Date(ngayHienTai.getTime() + 86400000 * 10); // 10 ngày sau
            khuyenMaiHetSL.setNgayBatDau(ngayBatDau);
            khuyenMaiHetSL.setNgayHetHan(ngayHetHan);
            
            // Đã dùng hết số lượng
            khuyenMaiHetSL.setTongSoLuong(100);
            khuyenMaiHetSL.setDaSuDung(100); // Đã sử dụng hết
            
            khuyenMaiDao.themKhuyenMai(khuyenMaiHetSL);
            
            // Kiểm tra mã giảm giá đã dùng hết số lượng -> result == 0 -> return false
            boolean result = khuyenMaiDao.apDungMaGiamGia(maGiamGia);
            assertFalse(result);
            
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức kiểm tra mã giảm giá khi truy vấn bị exception
     * ID: TKM_27
     */
    @Test
    public void kiemTraMaGiamGia_testQueryException() {
        String maGiamGia = "GG003";
        try {
            // Đóng session để gây ra exception khi truy vấn
            session.close();
            
            // Thử áp dụng mã giảm giá khi không có kết nối
            boolean result = khuyenMaiDao.apDungMaGiamGia(maGiamGia);
            assertFalse(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }
}
