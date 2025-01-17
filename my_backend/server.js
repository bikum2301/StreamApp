const express = require('express');
const mysql = require('mysql2');
const nodemailer = require('nodemailer');
const bodyParser = require('body-parser');
const cors = require('cors');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Kết nối MySQL
const db = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
});

db.connect((err) => {
    if (err) {
        console.error('Lỗi kết nối MySQL:', err);
    } else {
        console.log('Đã kết nối MySQL');
    }
});

// Cấu hình Nodemailer
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASS,
    },
});

// API Đăng ký
app.post('/api/register', async (req, res) => {
    const { email, password } = req.body;
    const otp = Math.floor(100000 + Math.random() * 900000).toString(); // Tạo OTP 6 chữ số

    const sql = 'INSERT INTO users (email, password, otp) VALUES (?, ?, ?)';
    db.query(sql, [email, password, otp], (err, result) => {
        if (err) {
            return res.status(500).json({ message: 'Đăng ký thất bại', error: err.message });
        }

        // Gửi OTP qua email
        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: email,
            subject: 'Xác thực OTP',
            text: `Mã OTP của bạn là: ${otp}`,
        };

        transporter.sendMail(mailOptions, (error, info) => {
            if (error) {
                return res.status(500).json({ message: 'Gửi OTP thất bại' });
            }
            res.status(200).json({ message: 'OTP đã được gửi qua email' });
        });
    });
});

// API Xác thực OTP
app.post('/api/verify-otp', async (req, res) => {
    const { email, otp } = req.body;

    const sql = 'SELECT * FROM users WHERE email = ? AND otp = ?';
    db.query(sql, [email, otp], (err, results) => {
        if (err) {
            return res.status(500).json({ message: 'Xác thực thất bại', error: err.message });
        }

        if (results.length > 0) {
            const updateSql = 'UPDATE users SET isVerified = true, otp = NULL WHERE email = ?';
            db.query(updateSql, [email], (err, result) => {
                if (err) {
                    return res.status(500).json({ message: 'Xác thực thất bại', error: err.message });
                }
                res.status(200).json({ message: 'Xác thực thành công' });
            });
        } else {
            res.status(400).json({ message: 'OTP không hợp lệ' });
        }
    });
});

// API Đăng nhập
app.post('/api/login', async (req, res) => {
    const { email, password } = req.body;

    const sql = 'SELECT * FROM users WHERE email = ? AND password = ?';
    db.query(sql, [email, password], (err, results) => {
        if (err) {
            return res.status(500).json({ message: 'Đăng nhập thất bại', error: err.message });
        }

        if (results.length > 0) {
            const user = results[0];
            if (user.isVerified) {
                res.status(200).json({ message: 'Đăng nhập thành công' });
            } else {
                res.status(400).json({ message: 'Tài khoản chưa được xác thực' });
            }
        } else {
            res.status(400).json({ message: 'Email hoặc mật khẩu không đúng' });
        }
    });
});

// API Quên mật khẩu
app.post('/api/forgot-password', async (req, res) => {
    const { email } = req.body;
    const otp = Math.floor(100000 + Math.random() * 900000).toString(); // Tạo OTP 6 chữ số

    const sql = 'UPDATE users SET otp = ? WHERE email = ?';
    db.query(sql, [otp, email], (err, result) => {
        if (err) {
            return res.status(500).json({ message: 'Quên mật khẩu thất bại', error: err.message });
        }

        if (result.affectedRows > 0) {
            // Gửi OTP qua email
            const mailOptions = {
                from: process.env.EMAIL_USER,
                to: email,
                subject: 'Đặt lại mật khẩu',
                text: `Mã OTP của bạn là: ${otp}`,
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    return res.status(500).json({ message: 'Gửi OTP thất bại' });
                }
                res.status(200).json({ message: 'OTP đã được gửi qua email' });
            });
        } else {
            res.status(404).json({ message: 'Người dùng không tồn tại' });
        }
    });
});

// API Đặt lại mật khẩu
app.post('/api/reset-password', async (req, res) => {
    const { email, otp, newPassword } = req.body;

    const sql = 'SELECT * FROM users WHERE email = ? AND otp = ?';
    db.query(sql, [email, otp], (err, results) => {
        if (err) {
            return res.status(500).json({ message: 'Đặt lại mật khẩu thất bại', error: err.message });
        }

        if (results.length > 0) {
            const updateSql = 'UPDATE users SET password = ?, otp = NULL WHERE email = ?';
            db.query(updateSql, [newPassword, email], (err, result) => {
                if (err) {
                    return res.status(500).json({ message: 'Đặt lại mật khẩu thất bại', error: err.message });
                }
                res.status(200).json({ message: 'Đặt lại mật khẩu thành công' });
            });
        } else {
            res.status(400).json({ message: 'OTP không hợp lệ' });
        }
    });
});

// Khởi động server
app.listen(PORT, () => {
    console.log(`Server đang chạy trên cổng ${PORT}`);
});