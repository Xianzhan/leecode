package xianzhan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xianzhan.dao.UserDao;
import xianzhan.pojo.User;
import xianzhan.service.IUserService;

/**
 * @author xianzhan
 * @since 2018-05-08
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int save(User user) {
        return userDao.insert(user);
    }

    @Override
    public User getById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int update(User user) {
        return userDao.updateByPrimaryKey(user);
    }
}
