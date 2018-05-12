package xianzhan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public int save(User user) {
        return userDao.insert(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int update(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }
}
