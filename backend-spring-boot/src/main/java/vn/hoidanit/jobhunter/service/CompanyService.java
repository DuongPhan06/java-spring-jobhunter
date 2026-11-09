package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.DTO.Meta;
import vn.hoidanit.jobhunter.domain.DTO.ResultPaginationDTO;
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

    public ResultPaginationDTO GetAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanys = this.companyRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        
        meta.setTotalPages(pageCompanys.getTotalPages());
        meta.setTotalElements(pageCompanys.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(pageCompanys.getContent());

        return resultPaginationDTO;
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
