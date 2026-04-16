-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM email_templates WHERE name = 'USER_ACCOUNT_VERIFICATION_AWAZ';

INSERT INTO `email_templates` (name, content, created_date, version)
VALUES
    (
        'USER_ACCOUNT_VERIFICATION_AWAZ',
        '<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>HAMRO AWAZ</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f6f8;">
            <div style="width: 100%; padding: 40px 0; display: flex; justify-content: center;">
                <div style="max-width: 500px; width: 90%; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 30px; text-align: center;">
                    <h1 style="color: #1a73e8; font-size: 28px; margin-bottom: 10px;">AWAZ</h1>
                    <h4 style="color: #333333; font-size: 18px; font-weight: normal; margin-bottom: 25px;">Verify Your Email to Get Started</h4>

                    <p style="color: #555555; font-size: 16px; line-height: 1.5; margin-bottom: 30px;">
                        You have successfully created an HAMRO AWAZ account. To activate your account, use the verification code below:
                    </p>

                    <div style="display: inline-block; padding: 20px 30px; background-color: #f1f5fb; border-radius: 8px; border: 1px solid #d1e3ff; margin-bottom: 30px;">
                        <span style="font-size: 32px; font-weight: bold; color: #1a73e8; letter-spacing: 4px;">${otp}</span>
                    </div>

                    <p style="color: #777777; font-size: 14px; margin-bottom: 5px;">
                        If you did not create this account, please ignore this email.
                    </p>
                    <p style="color: #1a73e8; font-weight: bold; font-size: 16px;">The HAMRO AWAZ Team</p>

                </div>
            </div>
        </body>
        </html>',
        NOW(),
        0
    ),
    (
        'ADMIN_ACCOUNT_VERIFICATION',
        '<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Admin Account Verification</title>
        </head>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
        <div style="width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;">

            <div style="text-align: center; padding: 15px 0; background-color: #1a73e8; color: #ffffff; border-radius: 8px 8px 0 0;">
                <h1 style="margin: 0; font-size: 24px;">HAMRO AWAZ Admin Verification</h1>
            </div>

            <div style="padding: 25px; line-height: 1.6; color: #333333;">
                <p>Dear ${adminName},</p>
                <p>HAMRO AWAZ admin account has been created successfully. To complete your registration, please verify your email address by clicking the button below:</p>

                <p style="text-align: center; margin: 30px 0;">
                    <a href="${verificationLink}" style="display: inline-block; background-color: #1a73e8; color: #ffffff; padding: 12px 25px; font-weight: bold; text-decoration: none; border-radius: 6px;">Verify Admin Account</a>
                </p>

                <p>If the button above does not work, copy and paste the following link into your browser:</p>
                <p style="word-break: break-all;"><a href="${verificationLink}" style="color: #1a73e8;">${verificationLink}</a></p>

                <p>If you did not request an admin account, please ignore this email or contact support immediately.</p>

                <p>Best regards,<br/><strong>The HAMRO AWAZ Team</strong></p>
            </div>

            <div style="text-align: center; padding: 15px 0; color: #888888; font-size: 12px; border-top: 1px solid #eeeeee;">
                <p>&copy; 2025 HAMRO AWAZ. All rights reserved.</p>
            </div>
        </div>
        </body>
        </html>',
        NOW(),
        0
    ),
    (
        'ESCALATION_EMAIL',
        '<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>HAMRO AWAZ - Complaint Escalated</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f6f8;">
            <div style="width: 100%; padding: 40px 0; display: flex; justify-content: center;">
                <div style="max-width: 520px; width: 90%; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 30px;">

                    <h1 style="color: #1a73e8; font-size: 28px; margin-bottom: 10px; text-align: center;">
                        HAMRO AWAZ
                    </h1>

                    <h4 style="color: #333333; font-size: 18px; font-weight: normal; margin-bottom: 25px; text-align: center;">
                        Complaint Priority Escalated
                    </h4>

                    <p style="color: #555555; font-size: 15px; line-height: 1.6; margin-bottom: 20px;">
                        This is to inform you that the following complaint has exceeded the defined resolution threshold and its priority has been escalated.
                    </p>

                    <div style="background-color: #f9fafb; border-radius: 8px; padding: 20px; border: 1px solid #e0e0e0; margin-bottom: 25px;">
                        <p style="margin: 8px 0; color: #333;"><strong>Complaint Title:</strong> ${complaintTitle}</p>
                      <p style="margin: 8px 0; color: #333;"><strong>Complaint Rule:</strong> ${complaintRule}</p>
                        <p style="margin: 8px 0; color: #333;"><strong>Category:</strong> ${category}</p>
                        <p style="margin: 8px 0; color: #333;"><strong>Created On:</strong> ${createdDate}</p>
                        <p style="margin: 8px 0; color: #d93025;"><strong>Current Priority:</strong> ${priority}</p>
                        <p style="margin: 8px 0; color: #333;"><strong>Escalated On:</strong> ${escalatedAt}</p>
                    </div>

                    <div style="background-color: #fff4e5; border-left: 5px solid #fb8c00; padding: 15px; border-radius: 6px; margin-bottom: 25px;">
                        <p style="margin: 0; font-size: 15px; color: #333;">
                            <strong>Assigned To:</strong> ${assignedTo}
                        </p>
                    </div>

                    <p style="color: #555555; font-size: 14px; line-height: 1.5; margin-bottom: 20px;">
                        Please take immediate action to address this complaint and ensure timely resolution to avoid further escalation.
                    </p>

                    <p style="color: #1a73e8; font-weight: bold; font-size: 15px; text-align: center;">
                        — The HAMRO AWAZ Team
                    </p>

                </div>
            </div>
        </body>
        </html>',
        NOW(),
        0
    ),
    (
        'GOVERNMENT_USER_ACCOUNT_VERIFICATION',
        '<!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Account Verification</title>
        </head>
        <body style="font-family: Arial, sans-serif; background-co  lor: #f4f4f4; margin: 0; padding: 0;">
        <div style="width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); border-radius: 8px;">

            <div style="text-align: center; padding: 15px 0; background-color: #1a73e8; color: #ffffff; border-radius: 8px 8px 0 0;">
                <h1 style="margin: 0; font-size: 24px;">Admin Verification</h1>
            </div>

            <div style="padding: 25px; line-height: 1.6; color: #333333;">
                <p>Dear ${userName},</p>
                <p>HAMRO AWAZ admin account has been created successfully. To complete your registration, please verify your email address by clicking the button below:</p>

                <p style="text-align: center; margin: 30px 0;">
                    <a href="${verificationLink}" style="display: inline-block; background-color: #1a73e8; color: #ffffff; padding: 12px 25px; font-weight: bold; text-decoration: none; border-radius: 6px;">Verify Admin Account</a>
                </p>

                <p>If the button above does not work, copy and paste the following link into your browser:</p>
                <p style="word-break: break-all;"><a href="${verificationLink}" style="color: #1a73e8;">${verificationLink}</a></p>

                <p>If you did not request an admin account, please ignore this email or contact support immediately.</p>

                <p>Best regards,<br/><strong>The HAMRO AWAZ Team</strong></p>
            </div>

            <div style="text-align: center; padding: 15px 0; color: #888888; font-size: 12px; border-top: 1px solid #eeeeee;">
                <p>&copy; {currentYear} HAMRO AWAZ. All rights reserved.</p>
            </div>
        </div>
        </body>
        </html>',
        NOW(),
        0
    ),
    (
        'ASSIGN_COMPLAINT_TO',
        '<!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>HAMRO AWAZ - Complaint Escalated</title>
    </head>
    <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f6f8;">
        <div style="width: 100%; padding: 40px 0; display: flex; justify-content: center;">
            <div style="max-width: 520px; width: 90%; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 30px;">

                <h1 style="color: #1a73e8; font-size: 28px; margin-bottom: 10px; text-align: center;">
                    HAMRO AWAZ
                </h1>

                <h4 style="color: #333333; font-size: 18px; font-weight: normal; margin-bottom: 25px; text-align: center;">
                    Complaint Assigned To You
                </h4>

                <p style="color: #555555; font-size: 15px; line-height: 1.6; margin-bottom: 20px;">
                    This is to inform you that the following complaint has been assign to you.
                </p>

                <div style="background-color: #f9fafb; border-radius: 8px; padding: 20px; border: 1px solid #e0e0e0; margin-bottom: 25px;">
                    <p style="margin: 8px 0; color: #333;"><strong>Complaint Title:</strong> ${complaintTitle}</p>
                  <p style="margin: 8px 0; color: #333;"><strong>Complaint Rule:</strong> ${complaintRule}</p>
                    <p style="margin: 8px 0; color: #333;"><strong>Category:</strong> ${category}</p>
                    <p style="margin: 8px 0; color: #333;"><strong>Created On:</strong> ${createdDate}</p>
                    <p style="margin: 8px 0; color: #d93025;"><strong>Current Priority:</strong> ${priority}</p>

                </div>

                <div style="background-color: #fff4e5; border-left: 5px solid #fb8c00; padding: 15px; border-radius: 6px; margin-bottom: 25px;">
                    <p style="margin: 0; font-size: 15px; color: #333;">
                        <strong>Assigned To:</strong> ${assignedTo}
                    </p>
                </div>

                <p style="color: #555555; font-size: 14px; line-height: 1.5; margin-bottom: 20px;">
                    Please take immediate action to address this complaint and ensure timely resolution to avoid escalation.
                </p>

                <p style="color: #1a73e8; font-weight: bold; font-size: 15px; text-align: center;">
                    — The HAMRO AWAZ Team
                </p>

            </div>
        </div>
    </body>
    </html>',
        NOW(),
        0
    );
