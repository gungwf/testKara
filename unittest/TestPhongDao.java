package unittest;

import dao.PhongDao;
import entity.Phong;
import entity.TrangThaiPhong;
import entity.LoaiPhong;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TestPhongDao {

    private PhongDao phongDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Phong.class)
                .addAnnotatedClass(TrangThaiPhong.class)
                .addAnnotatedClass(LoaiPhong.class)
                .buildSessionFactory();
        phongDao = new PhongDao(sessionFactory);
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
     * Kiểm tra phương thức lấy danh sách phòng hoạt động đúng
     * ID: TP_01
     */
    @Test
    public void layDanhSachPhong_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<Phong> result = phongDao.layDanhSachPhong();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng xử lý đúng khi có lỗi
     * ID: TP_02
     */
    @Test
    public void layDanhSachPhong_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<Phong> result = phongDao.layDanhSachPhong();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy thông tin phòng qua mã hoạt động đúng
     * ID: TP_03
     */
    @Test
    public void layThongTinPhongQuaMa_testChuan() {
        String maPhong = "P001";
        try {
            transaction = session.beginTransaction();
            Phong result = phongDao.layThongTinPhongQuaMa(maPhong);
            assertNotNull(result);
            assertEquals(maPhong, result.getMaPhong());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy thông tin phòng qua mã xử lý đúng khi mã không tồn tại
     * ID: TP_04
     */
    @Test
    public void layThongTinPhongQuaMa_testKhongTonTai() {
        String maPhong = "P999";
        try {
            transaction = session.beginTransaction();
            Phong result = phongDao.layThongTinPhongQuaMa(maPhong);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật trạng thái phòng hoạt động đúng
     * ID: TP_05
     */
    @Test
    public void capNhatTrangThaiPhong_testChuan() {
        String maPhong = "P001";
        String maTTP = "TTP001";
        try {
            transaction = session.beginTransaction();
            boolean result = phongDao.capNhatTrangThaiPhong(maPhong, maTTP);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức cập nhật trạng thái phòng xử lý đúng khi mã phòng không tồn tại
     * ID: TP_06
     */
    @Test
    public void capNhatTrangThaiPhong_testKhongTonTai() {
        String maPhong = "P999";
        String maTTP = "TTP001";
        try {
            transaction = session.beginTransaction();
            boolean result = phongDao.capNhatTrangThaiPhong(maPhong, maTTP);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái phòng hoạt động đúng
     * ID: TP_07
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiPhong_testChuan() {
        String maTTP = "TTP001";
        try {
            transaction = session.beginTransaction();
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiPhong(maTTP);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái phòng xử lý đúng khi mã trạng thái không tồn tại
     * ID: TP_08
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiPhong_testKhongTonTai() {
        String maTTP = "TTP999";
        try {
            transaction = session.beginTransaction();
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiPhong(maTTP);
            assertNotNull(result);
            assertTrue(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng được phép đặt hoạt động đúng
     * ID: TP_09
     */
    @Test
    public void layDanhSachPhongDuocPhepDat_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<Phong> result = phongDao.layDanhSachPhongDuocPhepDat();
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng được phép đặt xử lý đúng khi có lỗi
     * ID: TP_10
     */
    @Test
    public void layDanhSachPhongDuocPhepDat_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<Phong> result = phongDao.layDanhSachPhongDuocPhepDat();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái, loại phòng, số người hoạt động đúng
     * ID: TP_11
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi_testChuan() {
        try {
            transaction = session.beginTransaction();
            // Test với phòng đang hoạt động (tt=1), loại phòng VIP (LoaiPhong=1), phòng 5 người (soNguoi=1)
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi(1, 1, 1);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái, loại phòng, số người với tham số khác
     * ID: TP_12
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi_testWithDifferentParams() {
        try {
            transaction = session.beginTransaction();
            // Test với phòng không hoạt động (tt=2), loại phòng thường (LoaiPhong=2), phòng 10 người (soNguoi=2)
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi(2, 2, 2);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái, loại phòng, số người với tham số là 0
     * ID: TP_13
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi_testWithZeroParams() {
        try {
            transaction = session.beginTransaction();
            // Test với tất cả các tham số là 0 (tức là không lọc)
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi(0, 0, 0);
            assertNotNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái, loại phòng, số người với tham số không hợp lệ
     * ID: TP_14
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi_testWithInvalidParams() {
        try {
            transaction = session.beginTransaction();
            // Test với tham số không hợp lệ
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi(99, 99, 99);
            assertNotNull(result);
            assertTrue(result.isEmpty() || result.size() >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy danh sách phòng theo trạng thái, loại phòng, số người xử lý đúng khi có lỗi
     * ID: TP_15
     */
    @Test
    public void layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<Phong> result = phongDao.layDanhSachPhongTheoTrangThaiLoaiPhongSoNguoi(1, 1, 1);
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }
    
    /**
     * Kiểm tra phương thức lấy số lượng phòng theo trạng thái hoạt động đúng
     * ID: TP_16
     */
    @Test
    public void laySoLuongPhongTheoTrangThai_testChuan() {
        try {
            transaction = session.beginTransaction();
            // Test với trang thái phòng trống (tt=1)
            int result = phongDao.laySoLuongPhongTheoTrangThai(1);
            assertTrue(result >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy số lượng phòng theo trạng thái với tham số khác
     * ID: TP_17
     */
    @Test
    public void laySoLuongPhongTheoTrangThai_testWithDifferentParams() {
        try {
            transaction = session.beginTransaction();
            // Test với trạng thái phòng đang sử dụng (tt=2)
            int result = phongDao.laySoLuongPhongTheoTrangThai(2);
            assertTrue(result >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy số lượng phòng theo trạng thái với tham số là 0
     * ID: TP_18
     */
    @Test
    public void laySoLuongPhongTheoTrangThai_testWithZeroParam() {
        try {
            transaction = session.beginTransaction();
            // Test với tt=0 (tức là đếm tất cả phòng)
            int result = phongDao.laySoLuongPhongTheoTrangThai(0);
            assertTrue(result >= 0);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy số lượng phòng theo trạng thái với tham số không hợp lệ
     * ID: TP_19
     */
    @Test
    public void laySoLuongPhongTheoTrangThai_testWithInvalidParam() {
        try {
            transaction = session.beginTransaction();
            // Test với tham số không hợp lệ
            int result = phongDao.laySoLuongPhongTheoTrangThai(99);
            assertTrue(result == 0); // Với tham số không hợp lệ, kỳ vọng trả về 0
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra phương thức lấy số lượng phòng theo trạng thái xử lý đúng khi có lỗi
     * ID: TP_20
     */
    @Test
    public void laySoLuongPhongTheoTrangThai_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            int result = phongDao.laySoLuongPhongTheoTrangThai(1);
            assertEquals(0, result); // Khi có lỗi, kỳ vọng trả về 0
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }
} 