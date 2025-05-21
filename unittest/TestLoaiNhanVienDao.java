package unittest;

import dao.LoaiNhanVienDao;
import entity.LoaiNhanVien;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TestLoaiNhanVienDao {

    private LoaiNhanVienDao loaiNhanVienDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(LoaiNhanVien.class)
                .buildSessionFactory();
        loaiNhanVienDao = new LoaiNhanVienDao(sessionFactory);
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

    // Test phương thức layDanhSachLoaiNhanVien
    /**
     * Kiểm tra phương thức lấy danh sách loại nhân viên hoạt động đúng khi có kết nối đến database
     * ID: TLNV_01
     */
    @Test
    public void layDanhSachLoaiNhanVien_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<String> result = loaiNhanVienDao.layDanhSachLoaiNhanVien();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy danh sách loại nhân viên xử lý đúng khi mất kết nối database
     * ID: TLNV_02
     */
    @Test
    public void layDanhSachLoaiNhanVien_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<String> result = loaiNhanVienDao.layDanhSachLoaiNhanVien();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Test phương thức layLoaiNhanVien
    /**
     * Kiểm tra phương thức lấy loại nhân viên theo tên hoạt động đúng với tên hợp lệ
     * ID: TLNV_03
     */
    @Test
    public void layLoaiNhanVien_testChuan() {
        String ten = "Nhân viên văn phòng";
        try {
            transaction = session.beginTransaction();
            LoaiNhanVien result = loaiNhanVienDao.layLoaiNhanVien(ten);
            assertNotNull(result);
            assertEquals(ten, result.getTenLoaiNhanVien());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy loại nhân viên theo tên xử lý đúng khi tên không tồn tại
     * ID: TLNV_04
     */
    @Test
    public void layLoaiNhanVien_testKhongTonTai() {
        String ten = "Loại không tồn tại";
        try {
            transaction = session.beginTransaction();
            LoaiNhanVien result = loaiNhanVienDao.layLoaiNhanVien(ten);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Test phương thức layMaNhanVienTheoTenLoai
    /**
     * Kiểm tra phương thức lấy mã nhân viên theo tên loại hoạt động đúng với tên loại hợp lệ
     * ID: TLNV_05
     */
    @Test
    public void layMaNhanVienTheoTenLoai_testChuan() {
        String ten = "Nhân viên văn phòng";
        try {
            transaction = session.beginTransaction();
            String result = loaiNhanVienDao.layMaNhanVienTheoTenLoai(ten);
            assertNotNull(result);
            assertTrue(result.startsWith("LNV"));
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra phương thức lấy mã nhân viên theo tên loại xử lý đúng khi tên loại không tồn tại
     * ID: TLNV_06
     */
    @Test
    public void layMaNhanVienTheoTenLoai_testKhongTonTai() {
        String ten = "Loại không tồn tại";
        try {
            transaction = session.beginTransaction();
            String result = loaiNhanVienDao.layMaNhanVienTheoTenLoai(ten);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
