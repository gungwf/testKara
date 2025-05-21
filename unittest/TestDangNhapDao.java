package unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.DangNhapDao;
import dao.KhachHangDao;
import entity.DiaChi;
import entity.KhachHang;
import entity.NhanVien;
import view.DangNhap;

public class TestDangNhapDao {
	private KhachHangDao khachHangDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private DiaChi dc = new DiaChi();

    

    @Before
    public void setUp1() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(KhachHang.class)
                .buildSessionFactory();
        khachHangDao = new KhachHangDao(sessionFactory);
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown1() {
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();
    }


    private DangNhapDao dangNhapDao;

    @Before
    public void setUp() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(NhanVien.class)
                .buildSessionFactory();
        dangNhapDao = new DangNhapDao(sessionFactory);
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
//     Kiểm tra lấy đối tượng NhanVien theo số điện thoại hợp lệ
    //TDN_01
    @Test
    public void getNhanVienDangNhap_testChuan() {
        String sdt = "0987654321";
        try {
            transaction = session.beginTransaction();
            NhanVien result = dangNhapDao.getNhanVienDangNhap(sdt);
            assertNotNull(result);
            assertEquals(sdt, result.getSoDienThoai());
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra lấy đối tượng NhanVien với số điện thoại không có trong DB.
    //TDN_02
    @Test
    public void getNhanVienDangNhap_testKhongTonTai() {
        String sdt = "0000000000";
        try {
            transaction = session.beginTransaction();
            NhanVien result = dangNhapDao.getNhanVienDangNhap(sdt);
            assertNull(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra sửa tài khoản nhân viên với dữ liệu đúng chuẩn (có mã nhân viên, số điện thoại, password mới).
    //TDN_03
    @Test
    public void suaTaiKhoan_testChuan() {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNV("NV001");
        nhanVien.setSoDienThoai("0987654321");
        nhanVien.setPassword("newPassword123");
        try {
            transaction = session.beginTransaction();
            boolean result = dangNhapDao.suaTaiKhoan(nhanVien);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra sửa tài khoản khi truyền đối tượng NhanVien là null.
    //TDN_04
    @Test
    public void suaTaiKhoan_testNull() {
        try {
            transaction = session.beginTransaction();
            boolean result = dangNhapDao.suaTaiKhoan(null);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra kiểm tra sự tồn tại số điện thoại 0987654321 trong DB.
    //TDN_05
    @Test
    public void kiemTraSDT_testChuan() {
        String sdt = "0987654321";
        try {
            transaction = session.beginTransaction();
            boolean result = dangNhapDao.kiemTraSDT(sdt);
            assertTrue(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    //Kiểm tra kiểm tra sự tồn tại số điện thoại 0000000000 không có trong DB.
    //TDN_06
    @Test
    public void kiemTraSDT_testKhongTonTai() {
        String sdt = "0000000000";
        try {
            transaction = session.beginTransaction();
            boolean result = dangNhapDao.kiemTraSDT(sdt);
            assertFalse(result);
            transaction.rollback();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
}