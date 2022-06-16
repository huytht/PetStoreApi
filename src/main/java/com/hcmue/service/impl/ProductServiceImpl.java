package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.provider.file.MediaFile;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.provider.file.FileServiceFactory;
import com.hcmue.provider.file.FileType;
import com.hcmue.domain.FullTextSearchWithPagingParam;
import com.hcmue.constant.AppConstant;
import com.hcmue.constant.AppError;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageInfo;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.product.ProductShortDto;
import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.dto.user.UserWishList;
import com.hcmue.dto.product.ProductCreate;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.AppUserProduct;
import com.hcmue.entity.AppUserProductId;
import com.hcmue.entity.Breed;
import com.hcmue.entity.Category;
import com.hcmue.entity.Origin;
import com.hcmue.entity.Product;
import com.hcmue.entity.ProductImages;
import com.hcmue.provider.file.FileService;
import com.hcmue.repository.AppUserProductRepository;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.BreedRepository;
import com.hcmue.repository.CategoryRepository;
import com.hcmue.repository.OriginRepository;
import com.hcmue.repository.ProductRepository;
import com.hcmue.service.ProductService;
import com.hcmue.util.AppUtils;

@Service
public class ProductServiceImpl implements ProductService{

	private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private ProductRepository productRepository;
	private BreedRepository breedRepository;
	private CategoryRepository categoryRepository;
	private OriginRepository originRepository;
	private AppUserProductRepository userProductRepository;
	private AppUserRepository appUserRepository;
	private EntityManager entityManager;
	private FileService imageFileService;
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, BreedRepository breedRepository, CategoryRepository categoryRepository,
			OriginRepository originRepository, AppUserProductRepository userProductRepository,
			EntityManager entityManager, AppUserRepository appUserRepository) {
		this.productRepository = productRepository;
		this.breedRepository = breedRepository;
		this.categoryRepository = categoryRepository;
		this.originRepository = originRepository;
		this.userProductRepository = userProductRepository;
		this.appUserRepository = appUserRepository;
		this.entityManager = entityManager;
		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
	}

	@Override
	public AppServiceResult<List<ProductDto>> getProducts() {
		try {
			List<Product> products = productRepository.findAll();
			List<ProductDto> result = new ArrayList<ProductDto>();
			
			if (products != null && products.size() > 0)
				products.forEach(item -> result.add(ProductDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<ProductDto>>(true, 0, "Succeed!", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<ProductDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<ProductDto> getProductDetail(Long productId) {
		try {
			Product product = productRepository.findById(productId).orElse(null);

			return product == null
					? new AppServiceResult<ProductDto>(false, AppError.Validattion.errorCode(),
							"Product id is not exist: " + productId, null)
					: new AppServiceResult<ProductDto>(true, 0, "Succeed!", ProductDto.CreateFromEntity(product));

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<ProductDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<ProductDto> addProduct(ProductCreate product) throws UnsupportedFileTypeException {
		
		try {
			Product newProduct = new Product();
			newProduct.setName(product.getName());
			if (product.getBreedId() != null) {
				Breed breed = breedRepository.findById(product.getBreedId()).orElse(null);
				
				if (breed == null) {
					logger.warn("Breed Id: " + product.getBreedId() + " is not exist!");
					
					return new AppServiceResult<ProductDto>(false, AppError.Validattion.errorCode(),
							"Breed Id: " + product.getBreedId() + " is not exist!", null);
				} else
					newProduct.setBreed(breed);
			}
			
			if (product.getCategoryId() != null) {
				Category category  = categoryRepository.findById(product.getCategoryId()).orElse(null);
				
				if (category == null) {
					logger.warn("Category Id: " + product.getCategoryId() + " is not exist!");
					
					return new AppServiceResult<ProductDto>(false, AppError.Validattion.errorCode(),
							"Category Id: " + product.getCategoryId() + " is not exist!", null);
				} else
					newProduct.setCategory(category);
			}
			
			newProduct.setAmount(product.getAmount());
			newProduct.setPrice(product.getPrice());
			newProduct.setDescription(product.getDescription());
			
			if (product.getGender() != null)
				newProduct.setGender(product.getGender());
			
			if (product.getAge() != null)
				newProduct.setAge(product.getAge());
			 
			newProduct.setStatus(product.getStatus());
			
			if (product.getImageFile() != null) {
				for (MultipartFile file : product.getImageFile()) {
					MediaFile mediaFile = imageFileService.upload(newProduct.getName(), file);
					ProductImages productImages = new ProductImages();
					productImages.setImagePath(mediaFile.getPathUrl());
					productImages.setProduct(newProduct);
					newProduct.getProductImages().add(productImages);
				}
			}
			if (product.getOriginIds() != null)
				for (Long originId : product.getOriginIds()) {
					Origin origin = originRepository.findById(originId).orElse(null);
					if (origin == null) {
						logger.warn("Origin id is not exist: " + originId + ". Can not handle farther!");
	
						throw new IllegalArgumentException("Origin id is not exist: " + originId);
					} else
						newProduct.getOrigins().add(origin);
				}
			
			productRepository.save(newProduct);
			
			final ProductDto dto = ProductDto.CreateFromEntity(newProduct);
			
			
			return new AppServiceResult<ProductDto>(true, 0, "Succeed!", dto);
			
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();

			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

			return new AppServiceResult<ProductDto>(false, AppError.Validattion.errorCode(), e.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new AppServiceResult<ProductDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<PageDto<ProductShortDto>> getProductListByType(String typeOfProduct, PageParam pageParam) {
		try {
			Page<Product> products = typeOfProduct.equalsIgnoreCase(AppConstant.PRODUCT_NEW) 
									? productRepository.findAllByOrderByIdDesc(pageParam.getPageable()) 
									: productRepository.findAll(pageParam.getPageable());
			Page<ProductShortDto> dtoPage = products.map(item -> ProductShortDto.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<ProductShortDto>>(true, 0, "Succeed!", new PageDto<ProductShortDto>(dtoPage));
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<ProductShortDto>> getPetListByBreed(String typeOfProduct, Long breedId, PageParam pageParam) {
		try {
			Page<Product> products = typeOfProduct.equalsIgnoreCase(AppConstant.PRODUCT_NEW) 
									? productRepository.findAllByBreedIdOrderByIdDesc(breedId, pageParam.getPageable()) 
									: productRepository.findAllByBreedId(breedId, pageParam.getPageable());
			Page<ProductShortDto> dtoPage = products.map(item -> ProductShortDto.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<ProductShortDto>>(true, 0, "Succeed!", new PageDto<ProductShortDto>(dtoPage));
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<ProductShortDto>> getProductListByCategory(String typeOfProduct, Long categoryId, PageParam pageParam) {
		try {
			Page<Product> products = typeOfProduct.equalsIgnoreCase(AppConstant.PRODUCT_NEW) && categoryId != 0
									? productRepository.findAllByCategoryIdOrderByIdDesc(categoryId, pageParam.getPageable())
									: categoryId == 0
									? productRepository.findAllProductExceptPet(pageParam.getPageable())
									: productRepository.findAllByCategoryId(categoryId, pageParam.getPageable());
			Page<ProductShortDto> dtoPage = products.map(item -> ProductShortDto.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<ProductShortDto>>(true, 0, "Succeed!", new PageDto<ProductShortDto>(dtoPage));
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<RemarkProduct>> getRemarkListByProduct(Long productId, PageParam pageParam) {
		try {
			Page<AppUserProduct> remarks = userProductRepository.findAllByProductId(productId, pageParam.getPageable());
			
			Page<RemarkProduct> dtoPage = remarks.map(item -> RemarkProduct.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<RemarkProduct>>(true, 0, "Succeed!", new PageDto<RemarkProduct>(dtoPage));
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<RemarkProduct>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AppServiceResult<PageDto<ProductShortDto>> searchByFTS(FullTextSearchWithPagingParam params) {
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

			QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
					.forEntity(Product.class).get();

			org.apache.lucene.search.Query combinedQuery = queryBuilder.keyword()
					.onFields("name", "breed.name").matching(params.getText()).createQuery();

			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(combinedQuery,
					Product.class);
			jpaQuery.setFirstResult(params.getPageParam().getPageIndex() * params.getPageParam().getPageSize());
			jpaQuery.setMaxResults(params.getPageParam().getPageSize());
			
			List<Product> products = jpaQuery.getResultList();

			PageDto<ProductShortDto> result = new PageDto<ProductShortDto>();
			result.setContent(products.stream().map(item -> ProductShortDto.CreateFromEntity(item)).collect(Collectors.toList()));
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurrentPage(params.getPageParam().getPageIndex());
			pageInfo.setPageSize(params.getPageParam().getPageSize());
			pageInfo.setTotalElements((long) jpaQuery.getResultSize());

			result.setPageInfo(pageInfo);

			return new AppServiceResult<PageDto<ProductShortDto>>(true, 0, "Secceed!", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<ProductShortDto>> getWishList(PageParam pageParam) {
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			
			if (appUser == null) {
				logger.warn("Not logged in!");

				return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Validattion.errorCode(),
						"Not logged in!", null);
			}

			Page<Product> results = productRepository.findAllProductInWishList(appUser, pageParam.getPageable());
			Page<ProductShortDto> dtoPage = results.map(item -> ProductShortDto.CreateFromEntity(item));

			return new AppServiceResult<PageDto<ProductShortDto>>(true, 0, "Succeed!", new PageDto<ProductShortDto>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<ProductShortDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult updateWishList(Long productId) {
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			if (appUser == null) {
				logger.warn("Not logged in!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Not logged in!");
			}

			Product product = productRepository.findById(productId).orElse(null);

			if (product == null) {
				logger.warn("Product Id is not exist: " + productId + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Product Id is not exist: " + productId);
			}

			boolean existWishList = userProductRepository.existsByAppUserAndProduct(appUser, product);
			AppUserProduct newWL = new AppUserProduct();
			newWL.setAppUserProductId(new AppUserProductId(appUser.getId(), product.getId()));
			if (existWishList) {
				newWL = userProductRepository.findByAppUserProductId(newWL.getAppUserProductId());
				newWL.setFavourite(newWL.getFavourite() == null ? Boolean.TRUE : !newWL.getFavourite());
			} else {
				newWL.setAppUser(appUser);
				newWL.setProduct(product);
				newWL.setFavourite(Boolean.TRUE);
			}
			userProductRepository.save(newWL);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

}
