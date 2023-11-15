package com.codegym.repository;

import com.codegym.model.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
@Repository
public class CustomerRepository implements ICustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    //entityManager: Đây là một đối tượng EntityManager, một thành phần quan trọng trong JPA,
    // được sử dụng để quản lý các thao tác với cơ sở dữ liệu.
    @Override
    public List<Customer> findAll() {
        TypedQuery<Customer> query = entityManager.createQuery("select c from Customer c", Customer.class);
        return query.getResultList();
        //createQuery(...): Phương thức này của EntityManager được sử dụng để tạo một đối tượng truy vấn.
        // Trong trường hợp này, câu truy vấn được xây dựng bằng cách sử dụng JPQL (Java Persistence Query Language),
        // một ngôn ngữ truy vấn dựa trên đối tượng trong JPA.

        //TypedQuery là một interface trong JPA được sử dụng để đại diện cho các truy vấn đã được kiểu hóa
        // cho một loại cụ thể (trong trường hợp này là Customer).

        //Customer.class: Tham số thứ hai cho phương thức createQuery là loại của
        // đối tượng kết quả mà truy vấn sẽ trả về, trong trường hợp này là Customer.
    }

    @Override
    public Customer findById(Long id) {
        TypedQuery<Customer> query = entityManager.createQuery("select c from Customer c where c.id=:id", Customer.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Customer customer) {
        if (customer.getId() != null) {
            entityManager.merge(customer);
        } else {
            entityManager.persist(customer);
        }
        //Kiểm tra xem đối tượng khách hàng đã có một id hay chưa. Nếu id đã tồn tại,
        // có nghĩa là đối tượng này đã được lưu trữ trước đó và đang được cập nhật.

        //Nếu id đã tồn tại, sử dụng entityManager.merge(customer) để cập nhật đối tượng khách hàng
        // trong cơ sở dữ liệu. Phương thức merge sẽ cập nhật trạng thái của đối tượng đã tồn tại
        // trong cơ sở dữ liệu dựa trên thông tin của đối tượng được cung cấp. Nếu đối tượng chưa tồn tại
        // trong cơ sở dữ liệu, nó sẽ được thêm mới.

        //Nếu id không tồn tại, điều này có nghĩa là đối tượng khách hàng là mới và chưa được lưu trữ trước đó

        //Sử dụng entityManager.persist(customer) để thêm mới đối tượng khách hàng vào cơ sở dữ liệu.
        // Phương thức persist được sử dụng để đưa đối tượng mới vào quản lý của JPA để sau đó có thể được
        // đồng bộ hóa với cơ sở dữ liệu khi giao dịch được commit.

    }

    @Override
    public void remove(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            entityManager.remove(customer);
        }
    }

}
