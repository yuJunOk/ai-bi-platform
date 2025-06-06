import { Footer } from '@/components';
import {
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import { Helmet, history, Link, useModel } from '@umijs/max';
import { Alert, message, Space, Tabs } from 'antd';
import { createStyles } from 'antd-style';
import React, { useState } from 'react';
import Settings from '../../../../config/defaultSettings';
import {userLogin} from "@/services/multi-api-system/userController";
import {flushSync} from "react-dom";
const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});
const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};
const Login: React.FC = () => {
  const [userLoginState] = useState<API.LoginResult>({});
  const [type, setType] = useState<string>('account');
  const { setInitialState } = useModel('@@initialState');
  const { styles } = useStyles();
  const fetchUserInfo = (userInfo?: API.UserVo) => {
    if (userInfo) {
      flushSync(() => {
        //设置用户登录信息到全局对象中去
        setInitialState({loginUser: userInfo});
      });
    }
  };
  const handleSubmit = async (values: API.UserLoginDto) => {
    try {
      // 登录
      const res  = await userLogin({
        ...values
      })
      if (res.code === 23200) {
        message.success('登录成功！');
        fetchUserInfo(res.data);
        const urlParams = new URL(window.location.href).searchParams;
        history.push(urlParams.get('redirect') || '/');
        return;
      }else {
        throw new Error(res.message);
      }
    } catch (error: any) {
      message.error(error.message ?? '登录失败，请重试！');
    }
  };
  const { status, type: loginType } = userLoginState;
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'登录'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg" />}
          title="自定义管理系统"
          subTitle={'目前实现了一些自定义的管理功能'}
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.UserLoginDto);
          }}
        >
          <Tabs
            activeKey={type}
            onChange={setType}
            centered
            items={[
              {
                key: 'account',
                label: '账户密码登录',
              },
            ]}
          />

          {status === 'error' && loginType === 'account' && (
            <LoginMessage content={'错误的账号和密码'} />
          )}
          {type === 'account' && (
            <>
              <ProFormText
                name="loginName"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                placeholder={'请输入登录账号'}
                rules={[
                  {
                    required: true,
                    message: '账号是必填项！',
                  },
                  {
                    min: 4,
                    type: 'string',
                    message: '账号长度不能小于4！',
                  },
                ]}
              />
              <ProFormText.Password
                name="loginPwd"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'请输入登录密码'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    min: 8,
                    type: 'string',
                    message: '密码长度不能小于8！',
                  },
                ]}
              />
            </>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              自动登录
            </ProFormCheckbox>
            <Space split={'|'} style={{
              float: 'right',
            }}>
              <Link to={"/user/register"}>跳转注册</Link>
              <a>忘记密码 ?</a>
            </Space>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Login;
