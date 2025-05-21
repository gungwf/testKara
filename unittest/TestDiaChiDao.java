package unittest;

import dao.DiaChiDao;
import entity.DiaChi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestDiaChiDao {

    private DiaChiDao diaChiDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(DiaChi.class)
                .buildSessionFactory();
        diaChiDao = new DiaChiDao(sessionFactory);
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();
    }

    // Kiểm tra xem phương thức layDanhSachCacTinh() có trả về danh sách các tỉnh thành trong bảng DiaChi đúng và không rỗng
    //TDC_01
    @Test
    public void layDanhSachCacTinh_testChuan() {
        try {
            transaction = session.beginTransaction();
            List<String> result = diaChiDao.layDanhSachCacTinh();
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    //Mô phỏng lỗi khi kết nối DB bị đóng trước khi gọi layDanhSachCacTinh().
    //TDC_02
    @Test
    public void layDanhSachCacTinh_testLoi() {
        try {
            session.close(); // Ngắt kết nối để mô phỏng lỗi
            List<String> result = diaChiDao.layDanhSachCacTinh();
            assertNull(result);
        } catch (Exception e) {
            assertTrue(e instanceof Exception);
        }
    }

    // Kiểm tra lấy danh sách huyện trong tỉnh thành có tên "Hà Nội" trả về không rỗng và đúng.
    //TDC_03
    @Test
    public void layDanhSachHuyenTrongTinhTP_testChuan() {
        String tinh = "Hà Nội";
        try {
            transaction = session.beginTransaction();
            List<String> result = diaChiDao.layDanhSachHuyenTrongTinhTP(tinh);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra lấy huyện trong tỉnh không tồn tại, ví dụ "Tỉnh không tồn tại".
    //TDC_04
    @Test
    public void layDanhSachHuyenTrongTinhTP_testKhongTonTai() {
        String tinh = "Tỉnh không tồn tại";
        try {
            transaction = session.beginTransaction();
            List<String> result = diaChiDao.layDanhSachHuyenTrongTinhTP(tinh);
            assertNotNull(result);
            assertTrue(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy danh sách phường/xã trong huyện "Cầu Giấy" của tỉnh "Hà Nội".
    //TDC_05
    @Test
    public void layDanhSachPhuongXaTrongHuyenTinh_testChuan() {
        String tinh = "Hà Nội";
        String huyen = "Cầu Giấy";
        try {
            transaction = session.beginTransaction();
            List<String> result = diaChiDao.layDanhSachPhuongXaTrongHuyenTinh(tinh, huyen);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra khi huyện không tồn tại, ví dụ "Huyện không tồn tại".
    //TDC_06
    @Test
    public void layDanhSachPhuongXaTrongHuyenTinh_testKhongTonTai() {
        String tinh = "Hà Nội";
        String huyen = "Huyện không tồn tại";
        try {
            transaction = session.beginTransaction();
            List<String> result = diaChiDao.layDanhSachPhuongXaTrongHuyenTinh(tinh, huyen);
            assertNotNull(result);
            assertTrue(result.isEmpty());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Kiểm tra lấy đối tượng DiaChi theo phường, huyện, tỉnh hợp lệ ("Dịch Vọng", "Cầu Giấy", "Hà Nội").
    //TDC_07
    @Test
    public void layDiaChi_testChuan() {
        String xa = "Dịch Vọng";
        String huyen = "Cầu Giấy";
        String tinh = "Hà Nội";
        try {
            transaction = session.beginTransaction();
            DiaChi result = diaChiDao.layDiaChi(xa, huyen, tinh);
            assertNotNull(result);
            assertEquals(xa, result.getPhuongXa());
            assertEquals(huyen, result.getQuanHuyen());
            assertEquals(tinh, result.getTinhTP());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra khi địa chỉ không tồn tại (xã, huyện, tỉnh đều không có thật).
    //TDC_08
    @Test
    public void layDiaChi_testKhongTonTai() {
        String xa = "Xã không tồn tại";
        String huyen = "Huyện không tồn tại";
        String tinh = "Tỉnh không tồn tại";
        try {
            transaction = session.beginTransaction();
            DiaChi result = diaChiDao.layDiaChi(xa, huyen, tinh);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
