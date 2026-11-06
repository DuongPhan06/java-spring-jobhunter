package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company HandleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> GetAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Company HandleGetCompanyByID(long id) {
        Optional<Company> company = this.companyRepository.findById(id);
        if (company.isPresent()) {
            return company.get();
        } else {
            return null;
        }
    }

    public Company HandleUpdateCompany(Company ReqCompany) {
        Company CurrentCompany = HandleGetCompanyByID(ReqCompany.getId());
        if (CurrentCompany != null) {
            CurrentCompany.setAddress(ReqCompany.getAddress());
            CurrentCompany.setDescription(ReqCompany.getDescription());
            CurrentCompany.setLogo(ReqCompany.getLogo());
            CurrentCompany.setName(ReqCompany.getName());
            CurrentCompany.setUpdateAt(ReqCompany.getUpdateAt());
            CurrentCompany.setUpdateBy(ReqCompany.getUpdateBy());
            Company company = this.companyRepository.save(CurrentCompany);
            return company;
        }
        return null;
    }

    public void HandleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
