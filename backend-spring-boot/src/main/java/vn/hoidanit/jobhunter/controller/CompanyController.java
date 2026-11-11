package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.DTO.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> postCompany(@Valid @RequestBody Company company) {
        Company cpn = this.companyService.HandleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(cpn);
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch all Companies Successfully")
    public ResponseEntity<ResultPaginationDTO> FetchAllCompanies(
            @Filter Specification<Company> spec, Pageable pageable) {

        // Pageable pageable = PageRequest.of(page - 1, size);
        ResultPaginationDTO resultPaginationDTO = this.companyService.GetAllCompanies(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> GetCompanyByID(@PathVariable long id) {
        Company company = this.companyService.HandleGetCompanyByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> PutUpdateCompany(@RequestBody Company reqCompany) {
        Company company = this.companyService.HandleUpdateCompany(reqCompany);
        return ResponseEntity.ok().body(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> getDeleteCompany(@PathVariable long id) {
        this.companyService.HandleDeleteCompany(id);
        return ResponseEntity.ok().body("Delete Company Successfully");
    }

}
