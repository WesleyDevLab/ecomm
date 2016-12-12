package ru.ncedu.ecomm.data;

import ru.ncedu.ecomm.Configuration;
import ru.ncedu.ecomm.data.accessobjects.CategoryDAO;
import ru.ncedu.ecomm.data.accessobjects.PropertyDAO;
import ru.ncedu.ecomm.data.accessobjects.RoleDAO;
import ru.ncedu.ecomm.data.accessobjects.UserDAO;
import ru.ncedu.ecomm.data.accessobjects.impl.PostgresCategoryDAO;
import ru.ncedu.ecomm.data.accessobjects.impl.PostgresPropertyDAO;
import ru.ncedu.ecomm.data.accessobjects.impl.PostgresRoleDAO;
import ru.ncedu.ecomm.data.accessobjects.impl.PostgresUserDAO;

public abstract class DAOFactory {

    public abstract RoleDAO getRoleDAO();
    public abstract CategoryDAO getCategoryDAO();
    public abstract PropertyDAO getPropertyDAO();
    public abstract UserDAO getUserDAO();

    // public abstract CategoryDAO getCategoryDAO();
    // another DAO...

    public static DAOFactory getDAOFactory() {

        switch (Configuration.getProperty("db.type")) {
            case "postgresql":
                return new DAOFactory() {

                    @Override
                    public RoleDAO getRoleDAO() {
                        return new PostgresRoleDAO();
                    }

                    @Override
                    public CategoryDAO getCategoryDAO() {
                        return new PostgresCategoryDAO();
                    }

                    @Override
                    public PropertyDAO getPropertyDAO() {
                        return new PostgresPropertyDAO();
                    }

                    @Override
                    public UserDAO getUserDAO() {
                        return new PostgresUserDAO();
                    }
                };
            default:
                throw new UnsupportedOperationException("Unsupported DAO type");
        }
    }
}
