package xianzhan.service;

import xianzhan.pojo.User;

/**
 * @author xianzhan
 * @since 2018-05-08
 */
public interface IUserService {

    int save(User user);

    User getById(Integer id);

    int update(User user);
}
